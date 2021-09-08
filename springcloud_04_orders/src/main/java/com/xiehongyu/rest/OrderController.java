package com.xiehongyu.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/8/31 17:40
 */
@RestController
public class OrderController {

    @Value("${server.port}")
    private int port;
    @RequestMapping("/order")
    public String order(){
        System.out.println("order demo ...");

        return "order demo, 端口为：" + port;
    }
}
