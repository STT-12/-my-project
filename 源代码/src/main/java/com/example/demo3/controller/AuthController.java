package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.dto.LoginRequest;
import com.example.demo3.dto.LoginResponse;
import com.example.demo3.entity.User;
import com.example.demo3.service.UserService;
import com.example.demo3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getPrivilege());

            // 将头像信息也加入到LoginResponse DTO中 (需要修改LoginResponse)
            LoginResponse response = new LoginResponse(token, user.getUsername(), user.getPrivilege(), user.getAvatar());
            return ApiResponse.success(response, "登录成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody LoginRequest registerRequest) {
        try {
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(registerRequest.getPassword());
            User registeredUser = userService.register(newUser);
            return ApiResponse.success(registeredUser, "注册成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ApiResponse<Map<String, Object>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ApiResponse.error(401, "Token格式错误");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.error(401, "Token无效或已过期");
            }

            String username = jwtUtil.extractUsername(token);
            Integer userId = jwtUtil.extractUserId(token);
            Integer privilege = jwtUtil.extractPrivilege(token);

            // 返回更多用户信息，前端需要
            Map<String, Object> userInfo = Map.of(
                    "id", userId,
                    "username", username,
                    "privilege", privilege
            );

            return ApiResponse.success(userInfo, "Token验证成功");
        } catch (Exception e) {
            return ApiResponse.error(401, "Token验证失败");
        }
    }
}