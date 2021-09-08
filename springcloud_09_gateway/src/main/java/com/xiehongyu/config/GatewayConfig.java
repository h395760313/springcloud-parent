package com.xiehongyu.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/8 10:46
 */
@Configuration
public class GatewayConfig {

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
//        return builder.routes()
//                .route("category_router",r -> r.path("/category/**").uri("http://localhost:8807"))
//                .route("product_router",r -> r.path("/product/**").uri("http://localhost:8806"))
//                .build();
//    }
}
