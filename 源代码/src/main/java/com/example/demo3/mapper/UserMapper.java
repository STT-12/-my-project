package com.example.demo3.mapper;

import com.example.demo3.entity.User;
import com.example.demo3.dto.UserQuery;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("""
        <script>
        SELECT id, username, password, privilege, avatar
        FROM user
        WHERE 1=1
        <if test='query.keyword != null and query.keyword != ""'>
            AND username LIKE '%' || #{query.keyword} || '%'
        </if>
        <if test='query.privilege != null'>
            AND privilege = #{query.privilege}
        </if>
        ORDER BY id
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<User> findByQuery(@Param("query") UserQuery query,
                           @Param("offset") Integer offset,
                           @Param("limit") Integer limit);

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM user
        WHERE 1=1
        <if test='keyword != null and keyword != ""'>
            AND username LIKE '%' || #{keyword} || '%'
        </if>
        <if test='privilege != null'>
            AND privilege = #{privilege}
        </if>
        </script>
        """)
    Integer countByQuery(@Param("keyword") String keyword,
                         @Param("privilege") Integer privilege);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE user SET username = #{username}, privilege = #{privilege}, avatar = #{avatar} WHERE id = #{id}")
    int updateUser(User user);

    @Update("UPDATE user SET avatar = #{avatarUrl} WHERE id = #{userId}")
    int updateAvatar(@Param("userId") Integer userId, @Param("avatarUrl") String avatarUrl);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Integer id);

    @Insert("INSERT INTO user (username, password, privilege, avatar) VALUES (#{username}, #{password}, #{privilege}, #{avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}