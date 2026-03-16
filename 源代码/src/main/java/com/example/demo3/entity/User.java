package com.example.demo3.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer privilege;
    private String avatar;
    
    // 移除了 createTime 字段
}