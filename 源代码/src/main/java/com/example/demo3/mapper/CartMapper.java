package com.example.demo3.mapper;

import com.example.demo3.entity.CartItem;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CartMapper {

    @Select("""
        SELECT c.id, c.user_id, c.book_id, c.quantity, b.name, b.author, b.img, b.price
        FROM cart c
        JOIN book b ON c.book_id = b.id
        WHERE c.user_id = #{userId}
        ORDER BY c.id DESC
    """)
    List<CartItem> findByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND book_id = #{bookId}")
    CartItem findByUserAndBook(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Insert("INSERT INTO cart(user_id, book_id, quantity) VALUES(#{userId}, #{bookId}, #{quantity})")
    int insert(CartItem cartItem);

    @Update("UPDATE cart SET quantity = #{quantity} WHERE id = #{id}")
    int updateQuantity(CartItem cartItem);

    @DeleteProvider(type = CartSqlBuilder.class, method = "buildDeleteByIds")
    int deleteByIds(@Param("ids") List<Integer> ids, @Param("userId") Integer userId);

    // 用于动态SQL
    class CartSqlBuilder {
        public String buildDeleteByIds(@Param("ids") List<Integer> ids, @Param("userId") Integer userId) {
            StringBuilder sb = new StringBuilder("DELETE FROM cart WHERE user_id = #{userId} AND id IN (");
            for (int i = 0; i < ids.size(); i++) {
                sb.append("#{ids[").append(i).append("]}");
                if (i < ids.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    }
}