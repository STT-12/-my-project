package com.example.demo3.service;

import com.example.demo3.entity.Book;
import com.example.demo3.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;

    public List<Book> getFavorites(Integer userId) {
        return favoriteMapper.findFavoritesByUserId(userId);
    }

    @Transactional
    public void addFavorite(Integer userId, Integer bookId) {
        if (favoriteMapper.countByUserIdAndBookId(userId, bookId) == 0) {
            favoriteMapper.insert(userId, bookId);
        }
    }

    @Transactional
    public void removeFavorite(Integer userId, Integer bookId) {
        favoriteMapper.delete(userId, bookId);
    }
}