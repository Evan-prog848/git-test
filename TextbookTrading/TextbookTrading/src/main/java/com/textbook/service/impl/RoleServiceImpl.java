package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.entity.Role;
import com.textbook.entity.RoleMenu;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.RoleMapper;
import com.textbook.mapper.RoleMenuMapper;
import com.textbook.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Role> listAll() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, Constants.STATUS_NORMAL)
               .orderByAsc(Role::getId);
        return this.list(wrapper);
    }

    @Override
    public Page<Role> pageList(Integer pageNum, Integer pageSize, String keyword) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Role::getRoleName, keyword)
                   .or()
                   .like(Role::getRoleCode, keyword);
        }
        wrapper.orderByDesc(Role::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void addRole(Role role) {
        if (!StringUtils.hasText(role.getRoleName())) {
            throw new BusinessException("角色名称不能为空");
        }
        if (!StringUtils.hasText(role.getRoleCode())) {
            throw new BusinessException("角色编码不能为空");
        }

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, role.getRoleCode());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        if (role.getStatus() == null) {
            role.setStatus(Constants.STATUS_NORMAL);
        }
        this.save(role);
    }

    @Override
    public void updateRole(Role role) {
        if (role.getId() == null) {
            throw new BusinessException("角色ID不能为空");
        }
        Role existing = this.getById(role.getId());
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }

        if (StringUtils.hasText(role.getRoleCode()) && !role.getRoleCode().equals(existing.getRoleCode())) {
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Role::getRoleCode, role.getRoleCode())
                   .ne(Role::getId, role.getId());
            if (this.count(wrapper) > 0) {
                throw new BusinessException("角色编码已存在");
            }
            existing.setRoleCode(role.getRoleCode());
        }

        if (StringUtils.hasText(role.getRoleName())) {
            existing.setRoleName(role.getRoleName());
        }
        if (StringUtils.hasText(role.getDescription())) {
            existing.setDescription(role.getDescription());
        }
        if (role.getStatus() != null) {
            existing.setStatus(role.getStatus());
        }

        this.updateById(existing);
    }

    @Override
    public void deleteRole(Long id) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
        this.removeById(id);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<RoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(deleteWrapper);

        if (menuIds == null || menuIds.isEmpty()) {
            return;
        }

        List<RoleMenu> roleMenus = menuIds.stream()
                .distinct()
                .map(menuId -> {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    return roleMenu;
                })
                .collect(Collectors.toList());

        for (RoleMenu roleMenu : roleMenus) {
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        return roleMenuMapper.selectList(wrapper).stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }
}
