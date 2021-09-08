package com.xiehongyu.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/7 14:14
 */
@RestController
public class HystrixController {

    @GetMapping("hystrix")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public String hystrix(@RequestParam("id") Integer id){
        System.out.println("hystrix demo");
        if (id <= 0) {
            throw new RuntimeException("无效ID！");
        }
        return "hystrix ok!!!";
    }

    public String demoFallback(Integer id){
        return "当前活动过于火爆，服务已经熔断了！";
    }

    public String defaultFallback(){
        return "网络连接失败，请重试！";
    }
}
