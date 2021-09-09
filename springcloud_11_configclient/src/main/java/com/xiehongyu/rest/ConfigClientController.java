package com.xiehongyu.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/8 17:37
 */
@RestController
@RefreshScope // 作用：用来在不需要重启微服务的情况下，将当前scope域中信息刷新为最新配置信息
public class ConfigClientController {

    @Value("${name}")
    private String name;

    @GetMapping("demo")
    public String demo(){
        System.out.println("demo ok!");
        return "demo ok! name : " + name;
    }

}
