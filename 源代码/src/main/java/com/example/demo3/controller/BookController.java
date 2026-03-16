package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.dto.BookQuery;
import com.example.demo3.dto.PageResult;
import com.example.demo3.entity.Book;
import com.example.demo3.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/list-page")
    public ApiResponse<PageResult<Book>> getBookPaginate(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        BookQuery query = new BookQuery();
        query.setPage(page);
        query.setLimit(limit);
        return ApiResponse.success(bookService.getBooksByPage(query));
    }

    @GetMapping("/list-category")
    public ApiResponse<List<Book>> getBookByCategory(@RequestParam Integer categoryId) {
        return ApiResponse.success(bookService.getBooksByCategory(categoryId));
    }

    @GetMapping("/list-keyword")
    public ApiResponse<List<Book>> getBookByKeyword(@RequestParam String keyword) {
        return ApiResponse.success(bookService.getBooksByKeyword(keyword));
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Book>> getBook(@Validated BookQuery query) {
        return ApiResponse.success(bookService.getBooksByPage(query));
    }

}