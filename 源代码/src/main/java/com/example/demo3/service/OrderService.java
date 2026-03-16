package com.example.demo3.service;

import com.example.demo3.dto.OrderDetailResponse;
import com.example.demo3.entity.Address;
import com.example.demo3.entity.CartItem;
import com.example.demo3.entity.Order;
import com.example.demo3.entity.OrderItem;
import com.example.demo3.mapper.AddressMapper;
import com.example.demo3.mapper.CartMapper;
import com.example.demo3.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final AddressMapper addressMapper;

    @Transactional
    public Order createOrder(Integer userId, List<Integer> cartItemIds, Integer addressId) {
        List<CartItem> allCartItems = cartMapper.findByUserId(userId);
        List<CartItem> itemsToCheckout = allCartItems.stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .collect(Collectors.toList());

        if (itemsToCheckout.isEmpty()) {
            throw new RuntimeException("没有选择任何商品来创建订单");
        }

        if (addressMapper.findByIdAndUserId(addressId, userId) == null) {
            throw new RuntimeException("收货地址无效");
        }

        BigDecimal totalPrice = itemsToCheckout.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderSn(generateOrderSn());
        order.setTotalPrice(totalPrice);
        order.setStatus(0); // 待付款
        order.setAddressId(addressId);
        order.setCreateTime(new Date()); // 设置创建时间

        orderMapper.insertOrder(order);

        List<OrderItem> orderItems = itemsToCheckout.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setBookId(cartItem.getBookId());
            orderItem.setName(cartItem.getName());
            orderItem.setCover(cartItem.getImg());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        orderMapper.insertOrderItems(orderItems);

        List<Integer> idsToDelete = itemsToCheckout.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        cartMapper.deleteByIds(idsToDelete, userId);

        order.setItems(orderItems);
        return order;
    }

    public List<Order> getOrderList(Integer userId) {
        List<Order> orders = orderMapper.findOrdersByUserId(userId);
        orders.forEach(order -> {
            List<OrderItem> items = orderMapper.findOrderItemsByOrderId(order.getId());
            order.setItems(items);
        });
        return orders;
    }

    // 获取订单详情
    public OrderDetailResponse getOrderDetail(Integer orderId, Integer userId) {
        Order order = orderMapper.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在或无权访问");
        }
        List<OrderItem> items = orderMapper.findOrderItemsByOrderId(order.getId());
        order.setItems(items);

        Address address = addressMapper.findByIdAndUserId(order.getAddressId(), userId);

        return new OrderDetailResponse(order, address);
    }

    // 支付订单
    @Transactional
    public void payOrder(Integer orderId, Integer userId) {
        Order order = orderMapper.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }

        Date now = new Date();
        int updatedRows = orderMapper.updateOrderPayStatus(orderId, 1, now, userId);
        if (updatedRows == 0) {
            throw new RuntimeException("支付失败，请重试");
        }
    }

    // 催发货（用户自助发货）
    @Transactional
    public void urgeShipOrder(Integer orderId, Integer userId) {
        Order order = orderMapper.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 1) { // 必须是已付款状态
            throw new RuntimeException("只有已付款订单可以发货");
        }

        Date now = new Date();
        int updatedRows = orderMapper.updateOrderShipStatus(orderId, 2, now);
        if (updatedRows == 0) {
            throw new RuntimeException("发货失败");
        }
    }

    // 确认收货
    @Transactional
    public void confirmReceive(Integer orderId, Integer userId) {
        Order order = orderMapper.findOrderByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 2) { // 必须是已发货状态
            throw new RuntimeException("只有已发货订单可以确认收货");
        }

        Date now = new Date();
        int updatedRows = orderMapper.updateOrderReceiveStatus(orderId, 3, now);
        if (updatedRows == 0) {
            throw new RuntimeException("确认收货失败");
        }
    }

    private String generateOrderSn() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return timestamp + uuid;
    }
}