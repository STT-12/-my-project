package com.example.demo3.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private Integer privilege;
    private String avatar; // 新增

    public LoginResponse(String token, String username, Integer privilege, String avatar) {
        this.token = token;
        this.username = username;
        this.privilege = privilege;
        this.avatar = avatar;
    }
}