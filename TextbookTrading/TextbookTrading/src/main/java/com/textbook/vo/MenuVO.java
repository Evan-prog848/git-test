package com.textbook.vo;

import com.textbook.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 菜单树VO
 */
@Data
public class MenuVO {

    private Long id;

    private Long parentId;

    private String menuName;

    private String path;

    private String component;

    private String icon;

    private Integer sort;

    private Integer status;

    /**
     * 子菜单列表
     */
    private List<MenuVO> children;

    public static MenuVO fromEntity(Menu menu) {
        MenuVO vo = new MenuVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setMenuName(menu.getMenuName());
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setIcon(menu.getIcon());
        vo.setSort(menu.getSort());
        vo.setStatus(menu.getStatus());
        return vo;
    }
}
