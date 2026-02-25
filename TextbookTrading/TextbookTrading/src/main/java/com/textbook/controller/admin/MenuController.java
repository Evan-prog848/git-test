package com.textbook.controller.admin;

import com.textbook.common.Result;
import com.textbook.entity.Menu;
import com.textbook.service.MenuService;
import com.textbook.vo.MenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Api(tags = "后台-菜单管理模块")
@RestController
@RequestMapping("/api/admin/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("获取所有菜单列表")
    @GetMapping("/list")
    public Result<List<Menu>> list() {
        List<Menu> list = menuService.listAll();
        return Result.success(list);
    }

    @ApiOperation("获取菜单树形结构")
    @GetMapping("/tree")
    public Result<List<MenuVO>> tree() {
        List<MenuVO> tree = menuService.getMenuTree();
        return Result.success(tree);
    }

    @ApiOperation("根据角色ID获取菜单树")
    @GetMapping("/tree/{roleId}")
    public Result<List<MenuVO>> treeByRoleId(@PathVariable Long roleId) {
        List<MenuVO> tree = menuService.getMenuTreeByRoleId(roleId);
        return Result.success(tree);
    }

    @ApiOperation("添加菜单")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return Result.success();
    }

    @ApiOperation("更新菜单")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }
}
