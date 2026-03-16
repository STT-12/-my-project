package com.example.demo3.service;

import com.example.demo3.dto.BookQuery;
import com.example.demo3.dto.PageResult;
import com.example.demo3.entity.Book;
import com.example.demo3.entity.Category;
import com.example.demo3.mapper.BookMapper;
import com.example.demo3.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;

    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
    }

    public PageResult<Book> getBooksByPage(BookQuery query) {
        int offset = (query.getPage() - 1) * query.getLimit();
        List<Book> books = bookMapper.findByQuery(query, offset, query.getLimit());
        Integer total = bookMapper.countByQuery(query.getKeyword(), query.getCategoryId());
        return new PageResult<>(books, query.getPage(), query.getLimit(), total);
    }

    public List<Book> getBooksByCategory(Integer categoryId) {
        return bookMapper.findByCategoryId(categoryId);
    }

    public List<Book> getBooksByKeyword(String keyword) {
        return bookMapper.findByKeyword(keyword);
    }

}