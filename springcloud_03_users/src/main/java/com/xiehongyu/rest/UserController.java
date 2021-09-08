package com.xiehongyu.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/8/31 17:39
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping("/user")
    public String order(){
        System.out.println("user demo ...");
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject("http://localhost:8804/order", String.class);
//        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("ORDERS");
//        serviceInstances.forEach(serviceInstance->{
//            log.info("地址为：{}，主机为：{}，端口为：{}",serviceInstance.getUri(),serviceInstance.getHost(),serviceInstance.getPort());
//        });
//        ServiceInstance serviceInstance = loadBalancerClient.choose("ORDERS");
//        log.info("地址为：{}，主机为：{}，端口为：{}",serviceInstance.getUri(),serviceInstance.getHost(),serviceInstance.getPort());
        String resule = restTemplate.getForObject("http://ORDERS/order", String.class);
        return "调用Order服务成功：" + resule ;
    }
}
