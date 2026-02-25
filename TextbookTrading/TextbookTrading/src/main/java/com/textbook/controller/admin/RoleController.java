package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.Role;
import com.textbook.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Api(tags = "后台-角色管理模块")
@RestController
@RequestMapping("/api/admin/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("获取所有角色列表")
    @GetMapping("/all")
    public Result<List<Role>> all() {
        List<Role> list = roleService.listAll();
        return Result.success(list);
    }

    @ApiOperation("角色分页列表")
    @GetMapping("/list")
    public Result<Page<Role>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Role> page = roleService.pageList(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @ApiOperation("添加角色")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success();
    }

    @ApiOperation("更新角色")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    @ApiOperation("分配菜单权限")
    @PostMapping("/assign-menus/{roleId}")
    public Result<Void> assignMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        roleService.assignMenus(roleId, menuIds);
        return Result.success();
    }

    @ApiOperation("获取角色的菜单ID列表")
    @GetMapping("/menus/{roleId}")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }
}
