package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.entity.Category;
import com.example.demo3.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final BookService bookService;

    @GetMapping("/list")
    public ApiResponse<List<Category>> getAllCategory() {
        return ApiResponse.success(bookService.getAllCategories());
    }
}