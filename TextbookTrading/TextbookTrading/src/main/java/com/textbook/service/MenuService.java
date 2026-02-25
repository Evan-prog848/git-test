package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Menu;
import com.textbook.vo.MenuVO;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有菜单列表
     */
    List<Menu> listAll();

    /**
     * 获取菜单树形结构
     */
    List<MenuVO> getMenuTree();

    /**
     * 根据角色ID获取菜单树
     */
    List<MenuVO> getMenuTreeByRoleId(Long roleId);

    /**
     * 添加菜单
     */
    void addMenu(Menu menu);

    /**
     * 更新菜单
     */
    void updateMenu(Menu menu);

    /**
     * 删除菜单
     */
    void deleteMenu(Long id);
}
