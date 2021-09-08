package com.xiehongyu.rest;

import com.xiehongyu.openfeign.ProductClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/1 16:00
 */
@RestController
@Slf4j
public class CategoryController {


    @Autowired
    private ProductClient productClient;


    @GetMapping("/list")
    public String list(){
        return "list ok!";
    }
    @RequestMapping("/category")
    public String category(){
        log.info("category service ...");
//        String product = productClie
//        nt.product();
//        String list = productClient.list();
//        String result  = productClient.test("xiexie", 24);
//        String result = productClient.test1(123, "hahhh");
//        Product product = new Product(1, "嗨丝", 99.99, new Timestamp(new Date().getTime()));
//        String result = productClient.test2(product);
        String result = productClient.test4(Arrays.asList(new String[]{"12","13","14"}));
        return "category ok!  " + result;
    }
}
