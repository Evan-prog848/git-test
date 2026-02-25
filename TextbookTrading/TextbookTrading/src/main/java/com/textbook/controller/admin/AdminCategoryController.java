package com.textbook.controller.admin;

import com.textbook.common.Result;
import com.textbook.entity.Category;
import com.textbook.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台分类管理控制器
 */
@Api(tags = "后台-分类管理模块")
@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("获取分类列表")
    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> list = categoryService.listAll();
        return Result.success(list);
    }

    @ApiOperation("添加分类")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success();
    }

    @ApiOperation("更新分类")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return Result.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
