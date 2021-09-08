package com.xiehongyu.rest;

import com.xiehongyu.feignclients.HystrixClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/7 15:41
 */
@RestController
@Slf4j
public class TestDemoController {

    @Autowired
    private HystrixClient hystrixClient;

    @GetMapping("test")
    public String test(Integer id){
        System.out.println("test demo! id: " + id);
        String result = hystrixClient.hystrix(id);
        log.info("demo result : " + result);
        return "test ok! " +result;
    }
}
