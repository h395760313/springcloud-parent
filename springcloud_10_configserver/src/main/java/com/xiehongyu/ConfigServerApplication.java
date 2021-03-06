package com.xiehongyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/8 17:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
@ServletComponentScan(basePackages = "com.xiehongyu.filters")
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class,args);
    }
}
