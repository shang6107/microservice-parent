package com.microservice.redpackage.service.impl;

import com.microservice.redpackage.bean.UserRedPackage;
import com.microservice.redpackage.service.RedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 09:08:39
 */
@Service
public class RedPackageServiceImpl implements RedPackageService {

    private final static String PREFIX = "red_package_list_";

    private final static int TIME_SIZE = 100;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DataSource dataSource;

    @Async
    @Override
    public void saveUserRedPackageByRedis(Long id, Double unitAccount) {
        System.err.println("开始保存数据");
        Long start = System.currentTimeMillis();

        BoundListOperations boundListOperations = redisTemplate.boundListOps(PREFIX + id);
        Long size = boundListOperations.size();

        Long times = size % TIME_SIZE == 0 ? size / TIME_SIZE : size / TIME_SIZE + 1;

        int count = 0;

        List<UserRedPackage> userRedPackages = new ArrayList<>(TIME_SIZE);

        for (int i = 0; i < times; i++) {
            List userIdList = null;
            if (i == 0) {
                userIdList = boundListOperations.range(i * TIME_SIZE, (i + 1) * TIME_SIZE);
            } else {
                userIdList = boundListOperations.range(i * TIME_SIZE + 1, (i + 1) * TIME_SIZE);
            }

            userRedPackages.clear();

            for (int i1 = 0; i1 < userIdList.size(); i1++) {
                String args = userIdList.get(i1).toString();
                String[] arr = args.split("-");
                String userIdStr = arr[0];
                String timeStr = arr[1];
                Long userId = Long.parseLong(userIdStr);
                Long time = Long.parseLong(timeStr);
                UserRedPackage userRedPackage = new UserRedPackage();
                userRedPackage.setRedPackageId(id);
                userRedPackage.setUserId(userId);
                userRedPackage.setAmount(unitAccount);
                userRedPackage.setGrabTime(new Timestamp(time));
                userRedPackage.setNote("抢红包：" + id);
                userRedPackages.add(userRedPackage);
            }
            count += executeBatch(userRedPackages);

        }
        redisTemplate.delete(PREFIX + id);
        Long end = System.currentTimeMillis();
        System.err.println("保存数据结束， 共消耗：" + (end - start) + "毫秒， 共：" + count + "条数据被保存。");

    }

    private int executeBatch(List<UserRedPackage> userRedPackages) {
        Connection conn = null;
        Statement stmt = null;
        int[] count = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (UserRedPackage userRedPackage : userRedPackages) {
                String sql = "UPDATE t_red_package SET STOCK = STOCK - 1 WHERE ID = " + userRedPackage.getRedPackageId();
                String sql1 = "INSERT INTO t_user_red_package(RED_PACKAGE_ID, USER_ID, AMOUNT, GRAB_TIME, NOTE) "
                        + "VALUES("
                        + userRedPackage.getRedPackageId() + ", "
                        + userRedPackage.getUserId() + ", "
                        + userRedPackage.getAmount() + ", "
                        + "'" + format.format(userRedPackage.getGrabTime()) + "', "
                        + "'" + userRedPackage.getNote() + "')";
                stmt.addBatch(sql);
                stmt.addBatch(sql1);
            }
            count = stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return count.length / 2;
    }
}
