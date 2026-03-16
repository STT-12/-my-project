package com.example.demo3.service;

import com.example.demo3.dto.PageResult;
import com.example.demo3.dto.UserQuery;
import com.example.demo3.entity.User;
import com.example.demo3.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public PageResult<User> getUsersByPage(UserQuery query) {
        int offset = (query.getPage() - 1) * query.getLimit();
        List<User> users = userMapper.findByQuery(query, offset, query.getLimit());
        Integer total = userMapper.countByQuery(query.getKeyword(), query.getPrivilege());
        return new PageResult<>(users, query.getPage(), query.getLimit(), total);
    }

    @Transactional
    public void resetPassword(Integer id, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userMapper.updatePassword(id, encodedPassword);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userMapper.findById(id);
        if (user != null && user.getPrivilege() == 1) {
            throw new RuntimeException("不能删除管理员用户");
        }
        userMapper.deleteById(id);
    }

    @Transactional
    public void addUser(User user) {
        User existingUser = userMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    @Transactional
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    @Transactional
    public User register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名 '" + user.getUsername() + "' 已被注册");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPrivilege(0);
        userMapper.insert(user);
        return user;
    }
}