package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.LoginDTO;
import com.textbook.entity.Admin;
import com.textbook.vo.LoginVO;

/**
 * 管理员服务接口
 */
public interface AdminService extends IService<Admin> {

    /**
     * 管理员登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 获取当前管理员信息
     */
    Admin getCurrentAdmin();

    /**
     * 分页查询管理员列表
     */
    Page<Admin> pageList(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 添加管理员
     */
    void addAdmin(Admin admin);

    /**
     * 更新管理员
     */
    void updateAdmin(Admin admin);

    /**
     * 删除管理员
     */
    void deleteAdmin(Long id);

    /**
     * 修改管理员状态
     */
    void updateStatus(Long id, Integer status);
}
