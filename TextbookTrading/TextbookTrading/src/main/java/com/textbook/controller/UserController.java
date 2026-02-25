package com.textbook.controller;

import com.textbook.common.Result;
import com.textbook.dto.LoginDTO;
import com.textbook.dto.PasswordDTO;
import com.textbook.dto.RegisterDTO;
import com.textbook.dto.UserUpdateDTO;
import com.textbook.service.UserService;
import com.textbook.vo.LoginVO;
import com.textbook.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        LoginVO vo = userService.login(dto);
        return Result.success(vo);
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getCurrentUser() {
        UserVO vo = userService.getCurrentUser();
        return Result.success(vo);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public Result<Void> updateUserInfo(@RequestBody UserUpdateDTO dto) {
        userService.updateUserInfo(dto);
        return Result.success();
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody PasswordDTO dto) {
        userService.updatePassword(dto);
        return Result.success();
    }

    @ApiOperation("获取用户主页信息")
    @GetMapping("/profile/{userId}")
    public Result<UserVO> getUserProfile(@PathVariable Long userId) {
        UserVO vo = userService.getUserProfile(userId);
        return Result.success(vo);
    }
}
