package com.example.demo3.mapper;

import com.example.demo3.entity.Book;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO favor (user_id, book_id) VALUES (#{userId}, #{bookId})")
    int insert(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Delete("DELETE FROM favor WHERE user_id = #{userId} AND book_id = #{bookId}")
    int delete(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Select("""
        SELECT b.*, c.name as categoryName
        FROM book b
        JOIN favor f ON b.id = f.book_id
        LEFT JOIN category c ON b.category = c.id
        WHERE f.user_id = #{userId}
        ORDER BY f.id DESC
    """)
    List<Book> findFavoritesByUserId(@Param("userId") Integer userId);

    @Select("SELECT COUNT(*) FROM favor WHERE user_id = #{userId} AND book_id = #{bookId}")
    int countByUserIdAndBookId(@Param("userId") Integer userId, @Param("bookId") Integer bookId);
}