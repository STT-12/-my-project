package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.entity.Address;
import com.example.demo3.service.ProfileService;
import com.example.demo3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    private Integer getUserIdFromHeader(String authHeader) {
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    // --- 地址接口 ---
    @GetMapping("/address")
    public ApiResponse<List<Address>> getAddresses(@RequestHeader("Authorization") String authHeader) {
        Integer userId = getUserIdFromHeader(authHeader);
        return ApiResponse.success(profileService.getAddresses(userId));
    }

    @PostMapping("/address")
    public ApiResponse<Address> saveAddress(@RequestHeader("Authorization") String authHeader, @RequestBody Address address) {
        Integer userId = getUserIdFromHeader(authHeader);
        address.setUserId(userId);
        Address savedAddress;
        if (address.getId() == null) {
            savedAddress = profileService.addAddress(address);
        } else {
            savedAddress = profileService.updateAddress(address);
        }
        return ApiResponse.success(savedAddress);
    }

    @DeleteMapping("/address/{id}")
    public ApiResponse<Void> deleteAddress(@RequestHeader("Authorization") String authHeader, @PathVariable Integer id) {
        Integer userId = getUserIdFromHeader(authHeader);
        profileService.deleteAddress(id, userId);
        return ApiResponse.success(null, "删除成功");
    }

    @PostMapping("/address/default")
    public ApiResponse<Void> setDefaultAddress(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        profileService.setDefaultAddress(userId, payload.get("id"));
        return ApiResponse.success(null, "设置成功");
    }

    // --- 头像接口 ---
    @PostMapping("/avatar")
    public ApiResponse<Map<String, String>> uploadAvatar(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> payload) {
        try {
            Integer userId = getUserIdFromHeader(authHeader);
            String base64Data = payload.get("avatar");
            String newAvatarUrl = profileService.updateAvatar(userId, base64Data);
            return ApiResponse.success(Map.of("avatar", newAvatarUrl), "头像更新成功");
        } catch (IOException | IllegalArgumentException e) {
            return ApiResponse.error(500, "头像上传失败: " + e.getMessage());
        }
    }
}