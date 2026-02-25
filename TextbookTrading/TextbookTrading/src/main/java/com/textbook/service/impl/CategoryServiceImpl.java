package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.entity.Category;
import com.textbook.entity.Textbook;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.CategoryMapper;
import com.textbook.service.CategoryService;
import com.textbook.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    @Lazy
    private TextbookService textbookService;

    @Override
    public List<Category> listAll() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, Constants.STATUS_NORMAL)
               .orderByAsc(Category::getSort);
        return this.list(wrapper);
    }

    @Override
    public void addCategory(Category category) {
        category.setStatus(Constants.STATUS_NORMAL);
        this.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        this.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        // 检查是否有教材使用此分类
        LambdaQueryWrapper<Textbook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Textbook::getCategoryId, id);
        if (textbookService.count(wrapper) > 0) {
            throw new BusinessException("该分类下有教材，无法删除");
        }

        this.removeById(id);
    }
}
