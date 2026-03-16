// 在 com.example.demo3.dto 包下创建此文件
package com.example.demo3.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private List<Integer> itemIds;
    private Integer addressId;
}