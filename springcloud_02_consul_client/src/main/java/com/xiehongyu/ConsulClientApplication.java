package com.xiehongyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/8/31 16:40
 */
@SpringBootApplication
@EnableDiscoveryClient // 作用：通用服务注册客户端注解，代表 consul client zk
public class ConsulClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsulClientApplication.class,args);
    }

}