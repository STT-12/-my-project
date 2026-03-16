package com.example.demo3.service;

import com.example.demo3.dto.ReviewRequest;
import com.example.demo3.entity.Review;
import com.example.demo3.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    @Transactional
    public void addReview(Integer userId, ReviewRequest request) {
        // 检查是否已评价（同一个订单同一本书只能评价一次）
        if (reviewMapper.existsByOrderIdAndBookId(request.getOrderId(), request.getBookId(), userId) > 0) {
            throw new RuntimeException("您已经评价过这本书了");
        }

        Review review = new Review();
        review.setOrderId(request.getOrderId());
        review.setBookId(request.getBookId());
        review.setUserId(userId);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setCreateTime(new Date());

        reviewMapper.insert(review);
    }

    // 修复：获取书籍的所有评价（包括用户名和头像）
    public List<Map<String, Object>> getBookReviews(Integer bookId) {
        List<Map<String, Object>> reviews = reviewMapper.findByBookId(bookId);

        // 确保返回的数据结构正确
        if (reviews == null) {
            return new ArrayList<>();
        }

        return reviews;
    }

    // 新增：获取用户对某本书的评价
    public List<Review> getUserBookReviews(Integer bookId, Integer userId) {
        return reviewMapper.findByBookIdAndUserId(bookId, userId);
    }

    // 新增：获取订单的评价状态
    public Map<Integer, Boolean> getOrderReviewStatus(Integer orderId, Integer userId) {
        List<Review> reviews = reviewMapper.findByOrderId(orderId);
        Map<Integer, Boolean> reviewStatus = new HashMap<>();

        // 假设订单中有多个商品，我们需要知道每个商品是否已评价
        // 这里可以根据实际情况调整
        for (Review review : reviews) {
            if (review.getUserId().equals(userId)) {
                reviewStatus.put(review.getBookId(), true);
            }
        }

        return reviewStatus;
    }
}