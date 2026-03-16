package com.example.demo3.mapper;

import com.example.demo3.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT id, name FROM category ORDER BY id")
    List<Category> findAll();

    @Select("SELECT id, name FROM category WHERE id = #{id}")
    Category findById(Integer id);
}