package com.example.demo3.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItem {
    // 购物车表本身的字段
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private Integer quantity;

    // 关联查询Book表得到的字段
    private String name;
    private String author;
    private String img;
    private BigDecimal price;
}