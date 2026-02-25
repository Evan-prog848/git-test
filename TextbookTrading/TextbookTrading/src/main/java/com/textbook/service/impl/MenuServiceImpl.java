package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.entity.Menu;
import com.textbook.entity.RoleMenu;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.MenuMapper;
import com.textbook.mapper.RoleMenuMapper;
import com.textbook.service.MenuService;
import com.textbook.vo.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> listAll() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<MenuVO> getMenuTree() {
        List<Menu> menus = listAll();
        return buildMenuTree(menus);
    }

    @Override
    public List<MenuVO> getMenuTreeByRoleId(Long roleId) {
        List<Menu> menus = this.baseMapper.selectByRoleId(roleId);
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, m -> m, (a, b) -> a));

        List<Menu> allMenus = listAll();
        for (Menu menu : allMenus) {
            if (menuMap.containsKey(menu.getId())) {
                continue;
            }
            Long parentId = menu.getParentId();
            while (parentId != null && parentId != 0) {
                Menu parent = findById(allMenus, parentId);
                if (parent == null) {
                    break;
                }
                if (!menuMap.containsKey(parent.getId())) {
                    menuMap.put(parent.getId(), parent);
                }
                parentId = parent.getParentId();
            }
        }

        List<Menu> merged = new ArrayList<>(menuMap.values());
        return buildMenuTree(merged);
    }

    @Override
    public void addMenu(Menu menu) {
        if (menu.getStatus() == null) {
            menu.setStatus(Constants.STATUS_NORMAL);
        }
        this.save(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        if (menu.getId() == null) {
            throw new BusinessException("菜单ID不能为空");
        }
        this.updateById(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> childrenWrapper = new LambdaQueryWrapper<>();
        childrenWrapper.eq(Menu::getParentId, id);
        if (this.count(childrenWrapper) > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }

        LambdaQueryWrapper<RoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(RoleMenu::getMenuId, id);
        roleMenuMapper.delete(roleMenuWrapper);

        this.removeById(id);
    }

    private List<MenuVO> buildMenuTree(List<Menu> menus) {
        Map<Long, MenuVO> voMap = new HashMap<>();
        for (Menu menu : menus) {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu, vo);
            vo.setChildren(new ArrayList<>());
            voMap.put(vo.getId(), vo);
        }

        List<MenuVO> roots = new ArrayList<>();
        for (Menu menu : menus) {
            MenuVO vo = voMap.get(menu.getId());
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                roots.add(vo);
            } else {
                MenuVO parent = voMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                } else {
                    roots.add(vo);
                }
            }
        }

        sortMenuTree(roots);
        return roots;
    }

    private void sortMenuTree(List<MenuVO> menus) {
        menus.sort(Comparator.comparing(MenuVO::getSort, Comparator.nullsLast(Integer::compareTo)));
        for (MenuVO menu : menus) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortMenuTree(menu.getChildren());
            }
        }
    }

    private Menu findById(List<Menu> menus, Long id) {
        for (Menu menu : menus) {
            if (menu.getId().equals(id)) {
                return menu;
            }
        }
        return null;
    }
}
