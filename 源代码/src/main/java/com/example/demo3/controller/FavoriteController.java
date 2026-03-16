package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.entity.Book;
import com.example.demo3.service.FavoriteService;
import com.example.demo3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtUtil jwtUtil;

    private Integer getUserIdFromHeader(String authHeader) {
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    @GetMapping("/list")
    public ApiResponse<List<Book>> getFavorites(@RequestHeader("Authorization") String authHeader) {
        Integer userId = getUserIdFromHeader(authHeader);
        return ApiResponse.success(favoriteService.getFavorites(userId));
    }

    @PostMapping("/add")
    public ApiResponse<Void> addFavorite(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        favoriteService.addFavorite(userId, payload.get("bookId"));
        return ApiResponse.success(null, "收藏成功");
    }

    @PostMapping("/remove")
    public ApiResponse<Void> removeFavorite(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        favoriteService.removeFavorite(userId, payload.get("bookId"));
        return ApiResponse.success(null, "已取消收藏");
    }
}