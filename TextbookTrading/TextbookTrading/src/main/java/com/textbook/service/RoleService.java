package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取所有角色列表
     */
    List<Role> listAll();

    /**
     * 分页查询角色列表
     */
    Page<Role> pageList(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 添加角色
     */
    void addRole(Role role);

    /**
     * 更新角色
     */
    void updateRole(Role role);

    /**
     * 删除角色
     */
    void deleteRole(Long id);

    /**
     * 分配菜单权限
     */
    void assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 获取角色的菜单ID列表
     */
    List<Long> getRoleMenuIds(Long roleId);
}
