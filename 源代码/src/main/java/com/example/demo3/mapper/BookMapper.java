package com.example.demo3.mapper;

import com.example.demo3.entity.Book;
import com.example.demo3.dto.BookQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface BookMapper {

    @Select("""
        <script>
        SELECT b.id, b.name, b.author, b.publisher, b.price, b.img, 
               b.description, b.category, c.name as categoryName 
        FROM book b 
        LEFT JOIN category c ON b.category = c.id 
        WHERE 1=1
        <if test='query.keyword != null and query.keyword != ""'>
            AND (b.name LIKE '%' || #{query.keyword} || '%' 
                 OR b.author LIKE '%' || #{query.keyword} || '%' 
                 OR b.publisher LIKE '%' || #{query.keyword} || '%')
        </if>
        <if test='query.categoryId != null and query.categoryId != 0'>
            AND b.category = #{query.categoryId}
        </if>
        ORDER BY b.id 
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<Book> findByQuery(@Param("query") BookQuery query,
                           @Param("offset") Integer offset,
                           @Param("limit") Integer limit);

    @Select("""
        <script>
        SELECT COUNT(*) 
        FROM book b 
        WHERE 1=1
        <if test='keyword != null and keyword != ""'>
            AND (b.name LIKE '%' || #{keyword} || '%' 
                 OR b.author LIKE '%' || #{keyword} || '%' 
                 OR b.publisher LIKE '%' || #{keyword} || '%')
        </if>
        <if test='categoryId != null and categoryId != 0'>
            AND b.category = #{categoryId}
        </if>
        </script>
        """)
    Integer countByQuery(@Param("keyword") String keyword,
                         @Param("categoryId") Integer categoryId);

    @Select("""
        SELECT b.id, b.name, b.author, b.publisher, b.price, b.img, 
               b.description, b.category, c.name as categoryName 
        FROM book b 
        LEFT JOIN category c ON b.category = c.id 
        WHERE b.category = #{categoryId} 
        ORDER BY b.id
        """)
    List<Book> findByCategoryId(@Param("categoryId") Integer categoryId);

    @Select("""
        SELECT b.id, b.name, b.author, b.publisher, b.price, b.img, 
               b.description, b.category, c.name as categoryName 
        FROM book b 
        LEFT JOIN category c ON b.category = c.id 
        WHERE b.name LIKE '%' || #{keyword} || '%' 
           OR b.author LIKE '%' || #{keyword} || '%' 
           OR b.publisher LIKE '%' || #{keyword} || '%' 
        ORDER BY b.id
        """)

    List<Book> findByKeyword(@Param("keyword") String keyword);
}