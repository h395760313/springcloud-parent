package com.xiehongyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/7 15:40
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OpenfeignHystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenfeignHystrixApplication.class,args);
    }
}
