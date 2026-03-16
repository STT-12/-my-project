package com.example.demo3.dto;

import com.example.demo3.entity.Address;
import com.example.demo3.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)  // 添加这个注解
public class OrderDetailResponse extends Order {
    private Address address;

    public OrderDetailResponse(Order order, Address address) {
        // 复制Order的所有属性
        this.setId(order.getId());
        this.setUserId(order.getUserId());
        this.setOrderSn(order.getOrderSn());
        this.setTotalPrice(order.getTotalPrice());
        this.setStatus(order.getStatus());
        this.setAddressId(order.getAddressId());
        this.setCreateTime(order.getCreateTime());
        this.setUpdateTime(order.getUpdateTime());

        // 设置新增的时间字段
        this.setPayTime(order.getPayTime());
        this.setShipTime(order.getShipTime());
        this.setReceiveTime(order.getReceiveTime());

        this.setItems(order.getItems());
        // 设置地址
        this.address = address;
    }
}