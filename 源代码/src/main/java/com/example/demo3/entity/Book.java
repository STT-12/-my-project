package com.example.demo3.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Book {
    private Integer id;
    private String name;
    private String author;
    private String publisher;
    private BigDecimal price;
    private String img;
    private String description;
    private Integer category;  // 数据库字段名为category

    // 为了前端显示，添加categoryName字段
    private String categoryName;
}