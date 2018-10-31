package com.microservice.redpackage.controller;

import com.microservice.redpackage.service.UserRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 10:06:45
 */
@Controller
@CrossOrigin(origins = "*")
public class RedPackageController {

    @Autowired
    private UserRedPackageService userRedPackageService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("/grabRedPackageByRedis")
    @ResponseBody
    public Map<String, Object> grabRedPackageByRedis(Long redPackageId, Long userId) {
        Map<String, Object> map = new HashMap<>();
        Long aLong = userRedPackageService.grabRedPackageByRedis(redPackageId, userId);
        if (aLong > 0) {
            map.put("message", "抢红包成功");
        } else {
            map.put("message", "抢红包失败");
        }
        System.out.println(aLong);
        return map;
    }
}
