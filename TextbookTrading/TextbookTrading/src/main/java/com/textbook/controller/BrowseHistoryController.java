package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.service.BrowseHistoryService;
import com.textbook.vo.TextbookVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 浏览历史控制器
 */
@Api(tags = "浏览历史模块")
@RestController
@RequestMapping("/api/history")
public class BrowseHistoryController {

    @Autowired
    private BrowseHistoryService browseHistoryService;

    @ApiOperation("我的浏览历史")
    @GetMapping("/list")
    public Result<Page<TextbookVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<TextbookVO> page = browseHistoryService.myHistory(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("清空浏览历史")
    @DeleteMapping("/clear")
    public Result<Void> clear() {
        browseHistoryService.clearHistory();
        return Result.success();
    }

    @ApiOperation("删除指定浏览记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        browseHistoryService.deleteHistory(id);
        return Result.success();
    }
}
