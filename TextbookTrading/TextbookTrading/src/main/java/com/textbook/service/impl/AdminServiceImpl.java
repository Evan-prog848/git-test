package com.textbook.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.LoginDTO;
import com.textbook.entity.Admin;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.AdminMapper;
import com.textbook.service.AdminService;
import com.textbook.utils.JwtUtils;
import com.textbook.utils.UserContext;
import com.textbook.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 管理员服务实现类
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginVO login(LoginDTO dto) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, dto.getUsername());
        Admin admin = this.getOne(wrapper);

        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!admin.getPassword().equals(DigestUtil.md5Hex(dto.getPassword()))) {
            throw new BusinessException("用户名或密码错误");
        }

        if (admin.getStatus() != null && admin.getStatus() == Constants.STATUS_DISABLE) {
            throw new BusinessException("账号已被禁用");
        }

        String token = jwtUtils.generateToken(admin.getId(), admin.getUsername(), Constants.USER_TYPE_ADMIN);

        LoginVO vo = new LoginVO();
        vo.setId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setNickname(admin.getNickname());
        vo.setAvatar(admin.getAvatar());
        vo.setToken(token);
        vo.setUserType(Constants.USER_TYPE_ADMIN);
        return vo;
    }

    @Override
    public Admin getCurrentAdmin() {
        Long adminId = UserContext.getUserId();
        if (adminId == null) {
            throw new BusinessException("未登录");
        }
        Admin admin = this.getById(adminId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        return admin;
    }

    @Override
    public Page<Admin> pageList(Integer pageNum, Integer pageSize, String keyword) {
        Page<Admin> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Admin::getUsername, keyword)
                   .or()
                   .like(Admin::getNickname, keyword);
        }
        wrapper.orderByDesc(Admin::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void addAdmin(Admin admin) {
        if (!StringUtils.hasText(admin.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        if (!StringUtils.hasText(admin.getPassword())) {
            throw new BusinessException("密码不能为空");
        }

        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, admin.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        if (!StringUtils.hasText(admin.getNickname())) {
            admin.setNickname(admin.getUsername());
        }
        if (admin.getStatus() == null) {
            admin.setStatus(Constants.STATUS_NORMAL);
        }
        this.save(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        if (admin.getId() == null) {
            throw new BusinessException("管理员ID不能为空");
        }
        Admin existing = this.getById(admin.getId());
        if (existing == null) {
            throw new BusinessException("管理员不存在");
        }

        if (StringUtils.hasText(admin.getUsername()) && !admin.getUsername().equals(existing.getUsername())) {
            LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Admin::getUsername, admin.getUsername())
                   .ne(Admin::getId, admin.getId());
            if (this.count(wrapper) > 0) {
                throw new BusinessException("用户名已存在");
            }
            existing.setUsername(admin.getUsername());
        }

        if (StringUtils.hasText(admin.getPassword())) {
            existing.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        }
        if (StringUtils.hasText(admin.getNickname())) {
            existing.setNickname(admin.getNickname());
        }
        if (StringUtils.hasText(admin.getAvatar())) {
            existing.setAvatar(admin.getAvatar());
        }
        if (admin.getRoleId() != null) {
            existing.setRoleId(admin.getRoleId());
        }
        if (admin.getStatus() != null) {
            existing.setStatus(admin.getStatus());
        }

        this.updateById(existing);
    }

    @Override
    public void deleteAdmin(Long id) {
        this.removeById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Admin admin = this.getById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        admin.setStatus(status);
        this.updateById(admin);
    }
}
