package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.dto.CreateOrderRequest;
import com.example.demo3.dto.OrderDetailResponse;
import com.example.demo3.entity.Order;
import com.example.demo3.service.OrderService;
import com.example.demo3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    private Integer getUserIdFromHeader(String authHeader) {
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    @PostMapping("/create")
    public ApiResponse<Order> createOrder(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody CreateOrderRequest request) { // 修改：使用DTO
        Integer userId = getUserIdFromHeader(authHeader);
        Order order = orderService.createOrder(userId, request.getItemIds(), request.getAddressId());
        return ApiResponse.success(order, "订单创建成功");
    }

    @GetMapping("/list")
    public ApiResponse<List<Order>> getOrderList(@RequestHeader("Authorization") String authHeader) {
        Integer userId = getUserIdFromHeader(authHeader);
        List<Order> orders = orderService.getOrderList(userId);
        return ApiResponse.success(orders);
    }

    // 新增：获取订单详情接口
    @GetMapping("/detail/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrderDetail(@RequestHeader("Authorization") String authHeader,
                                                           @PathVariable Integer orderId) {
        Integer userId = getUserIdFromHeader(authHeader);
        OrderDetailResponse orderDetail = orderService.getOrderDetail(orderId, userId);
        return ApiResponse.success(orderDetail);
    }

    // 新增：支付接口
    @PostMapping("/pay")
    public ApiResponse<Void> payOrder(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        Integer orderId = payload.get("orderId");
        orderService.payOrder(orderId, userId);
        return ApiResponse.success(null, "支付成功");
    }

    // 在OrderController.java中添加：
    @PostMapping("/urge")
    public ApiResponse<Void> urgeShip(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        Integer orderId = payload.get("orderId");
        orderService.urgeShipOrder(orderId, userId);
        return ApiResponse.success(null, "发货成功");
    }

    @PostMapping("/receive")
    public ApiResponse<Void> confirmReceive(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody Map<String, Integer> payload) {
        Integer userId = getUserIdFromHeader(authHeader);
        Integer orderId = payload.get("orderId");
        orderService.confirmReceive(orderId, userId);
        return ApiResponse.success(null, "确认收货成功");
    }
}
