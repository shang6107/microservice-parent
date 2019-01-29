package com.microservice.redpackage.controller;

import com.microservice.redpackage.service.UserRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String index() {
        return "index";
    }

    @RequestMapping("ok")
    @ResponseBody
    public String ok() {
        return "ok";
    }

    @RequestMapping("/grabRedPackageByRedis")
    @ResponseBody
    public String grabRedPackageByRedis(@RequestParam("redPackageId") Long redPackageId
            , @RequestParam("userId") Long userId) {
        long start = System.currentTimeMillis();
        if (userRedPackageService.grabRedPackageByRedis(redPackageId, userId) > 0) {
            return "ok";
        }
        return "no";
    }

    @RequestMapping("/testAjax")
    @ResponseBody
    public String testAjax(@RequestParam("redPackageId") Long redPackageId
            , @RequestParam("userId") Long userId) {
        return "ok : " + userId + ", " + redPackageId;
    }

}
