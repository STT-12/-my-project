package com.example.demo3.controller;
import com.example.demo3.dto.ApiResponse;
import com.example.demo3.dto.ReviewRequest;
import com.example.demo3.service.ReviewService;
import com.example.demo3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @PostMapping("/add")
    public ApiResponse<Void> addReview(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody ReviewRequest request) {
        Integer userId = jwtUtil.extractUserId(authHeader.substring(7));
        reviewService.addReview(userId, request);
        return ApiResponse.success(null, "评价成功");
    }

    @GetMapping("/book/{bookId}")
    public ApiResponse<List<Map<String, Object>>> getBookReviews(@PathVariable Integer bookId) {
        return ApiResponse.success(reviewService.getBookReviews(bookId));
    }
}