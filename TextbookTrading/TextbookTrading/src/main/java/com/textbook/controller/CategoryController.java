package com.textbook.controller;

import com.textbook.common.Result;
import com.textbook.entity.Category;
import com.textbook.mapper.TextbookMapper;
import com.textbook.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类控制器
 */
@Api(tags = "分类模块")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TextbookMapper textbookMapper;

    @ApiOperation("获取分类列表")
    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> list = categoryService.listAll();
        return Result.success(list);
    }

    @ApiOperation("获取各分类教材数量统计")
    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> categoryStats() {
        List<Map<String, Object>> stats = textbookMapper.selectCategoryStats();
        return Result.success(stats);
    }
}
