package com.example.demo3.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Integer orderId;
    private Integer bookId;
    private Integer rating;
    private String content;
}