package com.example.demo3.service;

import com.example.demo3.entity.CartItem;
import com.example.demo3.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public List<CartItem> getCartItems(Integer userId) {
        return cartMapper.findByUserId(userId);
    }

    @Transactional
    public void addToCart(Integer userId, Integer bookId, Integer quantity) {
        CartItem existingItem = cartMapper.findByUserAndBook(userId, bookId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartMapper.updateQuantity(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setBookId(bookId);
            newItem.setQuantity(quantity);
            cartMapper.insert(newItem);
        }
    }

    @Transactional
    public void updateCartItem(Integer itemId, Integer quantity, Integer userId) {
        CartItem item = new CartItem();
        item.setId(itemId);
        item.setQuantity(quantity);
        // 此处可以加入校验，确保用户只能修改自己的购物车项
        cartMapper.updateQuantity(item);
    }

    @Transactional
    public void removeCartItems(List<Integer> ids, Integer userId) {
        if (ids != null && !ids.isEmpty()) {
            cartMapper.deleteByIds(ids, userId);
        }
    }
}