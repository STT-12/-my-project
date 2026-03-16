package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.entity.CartItem;
import com.example.demo3.service.CartService;
import com.example.demo3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    private Integer getUserIdFromHeader(String authHeader) {
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    @GetMapping("/list")
    public ApiResponse<List<CartItem>> getCart(@RequestHeader("Authorization") String authHeader) {
        Integer userId = getUserIdFromHeader(authHeader);
        return ApiResponse.success(cartService.getCartItems(userId));
    }

    @PostMapping("/add")
    public ApiResponse<Void> addToCart(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        cartService.addToCart(userId, payload.get("bookId"), payload.get("quantity"));
        return ApiResponse.success(null, "添加成功");
    }

    @PostMapping("/update")
    public ApiResponse<Void> updateCart(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        cartService.updateCartItem(payload.get("itemId"), payload.get("quantity"), userId);
        return ApiResponse.success(null, "更新成功");
    }

    @PostMapping("/remove")
    public ApiResponse<Void> removeCartItems(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, List<Integer>> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        cartService.removeCartItems(payload.get("ids"), userId);
        return ApiResponse.success(null, "删除成功");
    }
}