package com.xiehongyu.rest;

import com.xiehongyu.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/1 15:56
 */
@RestController
@Slf4j
public class ProductController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/product")
    public String product(HttpServletRequest request, String color){
        String header = request.getHeader("User-Name");
        System.out.println("获取到的请求头：" + header);
        System.out.println("获取到的请求参数 color：" + color);
        log.info("product service...");
        return "product 调用成功，端口是：" + port;
    }

    @GetMapping("/list")
    public String list(){
        log.info("list service...");
        return "list 调用成功，端口是：" + port;
    }

    @GetMapping("/test")
    public String test(@RequestParam("name") String name, @RequestParam("age") Integer age){
        log.info("接收到的参数为：name: {}, age: {}", name, age);
        return "queryString 调用成功，端口是：" + port;
    }

    @GetMapping("/test1/{id}/{name}")
    public String test1(@PathVariable("id") Integer id, @PathVariable("name") String name){
        log.info("接收到的参数为：id: {}, name: {}", id, name);
        return "path 调用成功，端口是：" + port;
    }

    @PostMapping("/test2")
    public String test2(@RequestBody Product product){
        log.info("接收到的对象为：{}",product);
        return "queryString 调用成功，端口是：" + port;
    }

    @GetMapping("/test3")
    public String test3(@RequestParam("ids") String[] ids){
        for (String id : ids) {
            log.info("接收到的参数为：{}",id);
        }
        return "queryString 调用成功，端口是：" + port;
    }

    @GetMapping("/test4")
    public String test4(@RequestParam("ids") List<String> ids) throws InterruptedException {
        for (String id : ids) {
            log.info("接收到的参数为：{}",id);
        }
        TimeUnit.SECONDS.sleep(2);
        return "queryString 调用成功，端口是：" + port;
    }
}
