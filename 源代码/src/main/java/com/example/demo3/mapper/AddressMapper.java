package com.example.demo3.mapper;

import com.example.demo3.entity.Address;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressMapper {
    @Select("SELECT * FROM address WHERE user_id = #{userId} ORDER BY is_default DESC, id DESC")
    List<Address> findByUserId(@Param("userId") Integer userId);

    // 新增：根据ID和用户ID查找，保证安全性
    @Select("SELECT * FROM address WHERE id = #{id} AND user_id = #{userId}")
    Address findByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    @Insert("INSERT INTO address(user_id, name, phone, province, city, district, detail, is_default) " +
            "VALUES(#{userId}, #{name}, #{phone}, #{province}, #{city}, #{district}, #{detail}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address address);

    @Update("""
        UPDATE address SET
        name=#{name}, phone=#{phone}, province=#{province}, city=#{city},
        district=#{district}, detail=#{detail}, is_default=#{isDefault}
        WHERE id=#{id} AND user_id=#{userId}
    """)
    int update(Address address);

    @Delete("DELETE FROM address WHERE id = #{id} AND user_id = #{userId}")
    int deleteById(@Param("id") Integer id, @Param("userId") Integer userId);

    @Update("UPDATE address SET is_default = 0 WHERE user_id = #{userId}")
    int clearDefault(@Param("userId") Integer userId);

    @Update("UPDATE address SET is_default = 1 WHERE id = #{id} AND user_id = #{userId}")
    int setDefault(@Param("id") Integer id, @Param("userId") Integer userId);
}