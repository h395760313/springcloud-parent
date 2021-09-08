package com.xiehongyu.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/8 17:37
 */
@RestController
public class ConfigClientController {

    @Value("${name}")
    private String name;

    @GetMapping("demo")
    public String demo(){
        System.out.println("demo ok!");
        return "demo ok! name : " + name;
    }

}
