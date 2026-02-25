package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取分类列表
     */
    List<Category> listAll();

    /**
     * 添加分类
     */
    void addCategory(Category category);

    /**
     * 更新分类
     */
    void updateCategory(Category category);

    /**
     * 删除分类
     */
    void deleteCategory(Long id);
}
