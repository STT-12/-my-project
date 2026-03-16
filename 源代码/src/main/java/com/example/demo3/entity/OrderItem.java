package com.example.demo3.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItem {
    private Integer id;
    private Integer orderId;
    private Integer bookId;
    private String name;
    private String cover;
    private BigDecimal price;
    private Integer quantity;
}