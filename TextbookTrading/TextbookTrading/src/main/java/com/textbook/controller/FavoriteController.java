package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.service.FavoriteService;
import com.textbook.vo.TextbookVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 */
@Api(tags = "收藏模块")
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @ApiOperation("添加收藏")
    @PostMapping("/add/{textbookId}")
    public Result<Void> add(@PathVariable Long textbookId) {
        favoriteService.addFavorite(textbookId);
        return Result.success();
    }

    @ApiOperation("取消收藏")
    @DeleteMapping("/remove/{textbookId}")
    public Result<Void> remove(@PathVariable Long textbookId) {
        favoriteService.removeFavorite(textbookId);
        return Result.success();
    }

    @ApiOperation("切换收藏状态")
    @PostMapping("/toggle/{textbookId}")
    public Result<Boolean> toggle(@PathVariable Long textbookId) {
        Boolean isFavorite = favoriteService.toggleFavorite(textbookId);
        return Result.success(isFavorite);
    }

    @ApiOperation("是否已收藏")
    @GetMapping("/check/{textbookId}")
    public Result<Boolean> check(@PathVariable Long textbookId) {
        Boolean isFavorite = favoriteService.isFavorite(textbookId);
        return Result.success(isFavorite);
    }

    @ApiOperation("我的收藏列表")
    @GetMapping("/list")
    public Result<Page<TextbookVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<TextbookVO> page = favoriteService.myFavorites(pageNum, pageSize);
        return Result.success(page);
    }
}
