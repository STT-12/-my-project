// ReviewMapper.java - 修复查询方法
package com.example.demo3.mapper;

import com.example.demo3.entity.Review;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {

    @Insert("INSERT INTO review(order_id, book_id, user_id, rating, content, create_time) " +
            "VALUES(#{orderId}, #{bookId}, #{userId}, #{rating}, #{content}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Review review);

    @Select("SELECT COUNT(*) FROM review WHERE order_id = #{orderId} AND book_id = #{bookId} AND user_id = #{userId}")
    int existsByOrderIdAndBookId(@Param("orderId") Integer orderId,
                                 @Param("bookId") Integer bookId,
                                 @Param("userId") Integer userId);

    // 修复：查询书籍的所有评价，按时间倒序排列
    @Select("""
        SELECT 
            r.id,
            r.order_id as orderId,
            r.book_id as bookId,
            r.user_id as userId,
            r.rating,
            r.content,
            r.create_time as createTime,
            u.username,
            u.avatar as userAvatar
        FROM review r 
        LEFT JOIN user u ON r.user_id = u.id 
        WHERE r.book_id = #{bookId} 
        ORDER BY r.create_time DESC
        """)
    List<Map<String, Object>> findByBookId(@Param("bookId") Integer bookId);

    // 新增：查询用户对某本书的所有评价
    @Select("""
        SELECT * FROM review 
        WHERE book_id = #{bookId} AND user_id = #{userId}
        ORDER BY create_time DESC
        """)
    List<Review> findByBookIdAndUserId(@Param("bookId") Integer bookId,
                                       @Param("userId") Integer userId);

    // 新增：查询订单的所有评价
    @Select("SELECT * FROM review WHERE order_id = #{orderId} ORDER BY book_id")
    List<Review> findByOrderId(@Param("orderId") Integer orderId);
}