package com.example.demo3.mapper;

import com.example.demo3.entity.Order;
import com.example.demo3.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders(user_id, order_sn, total_price, status, address_id, create_time) VALUES(#{userId}, #{orderSn}, #{totalPrice}, #{status}, #{addressId}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrder(Order order);

    @InsertProvider(type = OrderSqlBuilder.class, method = "buildInsertOrderItems")
    int insertOrderItems(@Param("items") List<OrderItem> items);

    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> findOrdersByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM orders WHERE id = #{orderId} AND user_id = #{userId}")
    Order findOrderByIdAndUserId(@Param("orderId") Integer orderId, @Param("userId") Integer userId);

    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> findOrderItemsByOrderId(@Param("orderId") Integer orderId);

    // 支付订单
    @Update("UPDATE orders SET status = #{status}, pay_time = #{payTime} WHERE id = #{id} AND user_id = #{userId}")
    int updateOrderPayStatus(@Param("id") Integer orderId,
                             @Param("status") Integer status,
                             @Param("payTime") Date payTime,
                             @Param("userId") Integer userId);

    // 发货订单
    @Update("UPDATE orders SET status = #{status}, ship_time = #{shipTime} WHERE id = #{id}")
    int updateOrderShipStatus(@Param("id") Integer orderId,
                              @Param("status") Integer status,
                              @Param("shipTime") Date shipTime);

    // 确认收货
    @Update("UPDATE orders SET status = #{status}, receive_time = #{receiveTime} WHERE id = #{id}")
    int updateOrderReceiveStatus(@Param("id") Integer orderId,
                                 @Param("status") Integer status,
                                 @Param("receiveTime") Date receiveTime);

    // 通用更新订单状态（兼容原有代码）
    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    int updateOrderStatus(Order order);

    class OrderSqlBuilder {
        public String buildInsertOrderItems(@Param("items") List<OrderItem> items) {
            StringBuilder sb = new StringBuilder("INSERT INTO order_item(order_id, book_id, name, cover, price, quantity) VALUES ");
            for (int i = 0; i < items.size(); i++) {
                sb.append("(#{items[").append(i).append("].orderId}, ")
                        .append("#{items[").append(i).append("].bookId}, ")
                        .append("#{items[").append(i).append("].name}, ")
                        .append("#{items[").append(i).append("].cover}, ")
                        .append("#{items[").append(i).append("].price}, ")
                        .append("#{items[").append(i).append("].quantity})");
                if (i < items.size() - 1) sb.append(", ");
            }
            return sb.toString();
        }
    }
}