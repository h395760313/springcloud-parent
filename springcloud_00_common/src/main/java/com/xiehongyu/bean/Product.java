package com.xiehongyu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: xiehongyu
 * @Date: 2021/9/2 10:01
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer id;
    private String name;
    private Double price;
    private Timestamp date;
}
