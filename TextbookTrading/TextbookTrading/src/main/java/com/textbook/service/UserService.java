package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.LoginDTO;
import com.textbook.dto.PasswordDTO;
import com.textbook.dto.RegisterDTO;
import com.textbook.dto.UserUpdateDTO;
import com.textbook.entity.User;
import com.textbook.vo.LoginVO;
import com.textbook.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser();

    /**
     * 更新用户信息
     */
    void updateUserInfo(UserUpdateDTO dto);

    /**
     * 修改密码
     */
    void updatePassword(PasswordDTO dto);

    /**
     * 获取用户主页信息
     */
    UserVO getUserProfile(Long userId);

    /**
     * 分页查询用户列表（后台）
     */
    Page<User> pageList(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 禁用/启用用户
     */
    void updateStatus(Long userId, Integer status);

    /**
     * 获取用户详情（后台）
     */
    java.util.Map<String, Object> getUserDetail(Long userId);

    /**
     * 管理员添加用户
     */
    void adminAddUser(User user);

    /**
     * 重置用户密码
     */
    void resetPassword(Long userId);
}
