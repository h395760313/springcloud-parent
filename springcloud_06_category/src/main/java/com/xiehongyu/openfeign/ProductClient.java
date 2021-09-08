package com.xiehongyu.openfeign;

import com.xiehongyu.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "PRODUCT")
public interface ProductClient {

    @RequestMapping("/product")
    String product();

    @RequestMapping("/list")
    String list();

    @GetMapping("/test")
    String test(@RequestParam("name") String name, @RequestParam("age") Integer age);

    @GetMapping("/test1/{id}/{name}")
    String test1(@PathVariable("id") Integer id, @PathVariable("name") String name);

    @PostMapping("/test2")
    String test2(@RequestBody Product product);

    @GetMapping("/test3")
    String test3(@RequestParam("ids") String[] ids);

    @GetMapping("/test4")
    String test4(@RequestParam("ids") List<String> ids);
}
