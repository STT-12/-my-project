package com.example.demo3.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Review {
    private Integer id;
    private Integer orderId;
    private Integer bookId;
    private Integer userId;
    private Integer rating;
    private String content;
    private Date createTime;

    // 用于关联查询的字段（非数据库字段）
    private String username;
    private String userAvatar;
}