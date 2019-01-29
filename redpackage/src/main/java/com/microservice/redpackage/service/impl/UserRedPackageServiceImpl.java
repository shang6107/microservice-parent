package com.microservice.redpackage.service.impl;

import com.microservice.redpackage.service.RedPackageService;
import com.microservice.redpackage.service.UserRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 09:53:52
 */
@Service
public class UserRedPackageServiceImpl implements UserRedPackageService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedPackageService redPackageService;

    String script = "local listKey = 'red_package_list_'..KEYS[1] \n"
            + "local redPackage = 'red_package_'..KEYS[1] \n"
            + "local stock = tonumber(redis.call('hget', redPackage, 'stock')) \n"
            + "if stock <= 0 then return 0 end \n"
            + "stock = stock - 1 \n"
            + "redis.call('hset', redPackage, 'stock', tostring(stock)) \n"
            + "redis.call('rpush', listKey, ARGV[1]) \n"
            + "if stock == 0 then return 2 end \n"
            + "return 1 \n";
    String sha1;

    AtomicInteger count = new AtomicInteger(0);

    @Override
    public Long grabRedPackageByRedis(Long redPackageId, Long userId) {
        String args = userId + "-" + System.currentTimeMillis();
        Long result = null;
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try {
            if (sha1 == null) {
                sha1 = jedis.scriptLoad(script);
            }
            Object evalsha = jedis.evalsha(sha1, 1, "p", args);
            result = (Long) evalsha;
            if (result == 2) {
                String unitAmountStr = jedis.hget("red_package_p", "unit_amount");
                Double v = Double.parseDouble(unitAmountStr);
                System.err.println("thread_name = " + Thread.currentThread().getName());
                redPackageService.saveUserRedPackageByRedis(redPackageId, v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
//        System.out.println("第" + (count.addAndGet(1)) + "次请求");
//        System.out.println("请求结果：" + result);
        return result;
    }
}
