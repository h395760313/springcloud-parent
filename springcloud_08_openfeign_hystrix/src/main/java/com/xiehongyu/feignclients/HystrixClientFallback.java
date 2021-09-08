package com.xiehongyu.feignclients;

import org.springframework.stereotype.Component;

/**
 * @Description: 自定义HystrixClient默认备选处理
 * @Author: xiehongyu
 * @Date: 2021/9/7 15:43
 */
@Component
public class HystrixClientFallback implements HystrixClient{

    @Override
    public String hystrix(Integer id){
        return "当前服务不可用，请稍后再试! id:" + id;
    }
}
