package com.xiehongyu.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/7 15:43
 */
@FeignClient(value = "HYSTRIX", fallback = HystrixClientFallback.class)
public interface HystrixClient {

    @GetMapping("hystrix")
    String hystrix(@RequestParam("id") Integer id);
}
