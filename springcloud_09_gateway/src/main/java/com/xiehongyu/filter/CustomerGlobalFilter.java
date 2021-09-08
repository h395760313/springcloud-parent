package com.xiehongyu.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: 自定义网关全局Filter
 * @Author: xiehongyu
 * @Date: 2021/9/8 16:17
 */
@Configuration
public class CustomerGlobalFilter implements GlobalFilter, Ordered {


    /**
     * 类似javaWeb的 doFilter
     * @param exchange 交换，封装了request、response
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 相当于httpServletRequest
        ServerHttpRequest request = exchange.getRequest();
        // 相当于httpServletResponse
        ServerHttpResponse response = exchange.getResponse();
        System.out.println("经过全局Filter处理。。。");
        Mono<Void> filter = chain.filter(exchange);
        System.out.println("响应回来Filter处理。。。");
        return filter;
    }

    /**
     * order 排序
     * @return 用来指定filter执行顺序，默认顺序按照自然数字进行排序  -1 在所有filter之前执行
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
