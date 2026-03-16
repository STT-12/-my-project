package com.example.demo3.controller;

import com.example.demo3.dto.ApiResponse;
import com.example.demo3.dto.PageResult;
import com.example.demo3.dto.UserQuery;
import com.example.demo3.entity.User;
import com.example.demo3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping("/page")
    public ApiResponse<Map<String, Object>> adminGetUsers(@Validated UserQuery query) {
        PageResult<User> result = userService.getUsersByPage(query);

        // 为了完全匹配前端的数据结构
        return ApiResponse.success(Map.of(
                "data", Map.of(
                        "data", Map.of(
                                "records", result.getRecords(),
                                "pageNumber", result.getPageNumber(),
                                "pageSize", result.getPageSize(),
                                "totalRow", result.getTotalRow(),
                                "totalPage", result.getTotalPage()
                        )
                )
        ));
    }

    @PostMapping("/reset")
    public ApiResponse<Void> adminResetUserPassword(@RequestBody Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        String password = (String) request.get("password");
        userService.resetPassword(id, password);
        return ApiResponse.success(null, "密码重置成功");
    }

    @PostMapping("/remove")
    public ApiResponse<Void> adminRemoveUser(@RequestBody Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        userService.deleteUser(id);
        return ApiResponse.success(null, "用户删除成功");
    }

    @PostMapping("/add")
    public ApiResponse<Void> adminAddUser(@RequestBody User user) {
        userService.addUser(user);
        return ApiResponse.success(null, "用户添加成功");
    }

    @PostMapping("/update")
    public ApiResponse<Void> adminUpdateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ApiResponse.success(null, "用户信息更新成功");
    }
}