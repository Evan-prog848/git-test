package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.User;
import com.textbook.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户管理控制器
 */
@Api(tags = "后台-用户管理模块")
@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户列表")
    @GetMapping("/list")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<User> page = userService.pageList(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @ApiOperation("禁用/启用用户")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("用户详情")
    @GetMapping("/detail/{id}")
    public Result<java.util.Map<String, Object>> getUserDetail(@PathVariable Long id) {
        java.util.Map<String, Object> detail = userService.getUserDetail(id);
        return Result.success(detail);
    }

    @ApiOperation("添加用户")
    @PostMapping("/add")
    public Result<Void> addUser(@RequestBody User user) {
        userService.adminAddUser(user);
        return Result.success();
    }

    @ApiOperation("重置用户密码")
    @PutMapping("/reset-password/{id}")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success();
    }
}
