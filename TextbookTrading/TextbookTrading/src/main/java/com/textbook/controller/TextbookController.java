package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.TextbookDTO;
import com.textbook.dto.TextbookQueryDTO;
import com.textbook.service.BrowseHistoryService;
import com.textbook.service.TextbookService;
import com.textbook.vo.TextbookVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 教材控制器
 */
@Api(tags = "教材模块")
@RestController
@RequestMapping("/api/textbook")
public class TextbookController {

    @Autowired
    private TextbookService textbookService;

    @Autowired
    private BrowseHistoryService browseHistoryService;

    @ApiOperation("发布教材")
    @PostMapping("/publish")
    public Result<Long> publish(@Valid @RequestBody TextbookDTO dto) {
        Long id = textbookService.publish(dto);
        return Result.success(id);
    }

    @ApiOperation("更新教材")
    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody TextbookDTO dto) {
        textbookService.update(dto);
        return Result.success();
    }

    @ApiOperation("教材列表")
    @GetMapping("/list")
    public Result<Page<TextbookVO>> list(TextbookQueryDTO query) {
        Page<TextbookVO> page = textbookService.pageList(query);
        return Result.success(page);
    }

    @ApiOperation("教材详情")
    @GetMapping("/detail/{id}")
    public Result<TextbookVO> detail(@PathVariable Long id) {
        // 增加浏览量
        textbookService.increaseViewCount(id);
        // 添加浏览记录
        try {
            browseHistoryService.addHistory(id);
        } catch (Exception ignored) {
            // 未登录时忽略
        }
        TextbookVO vo = textbookService.getDetail(id);
        return Result.success(vo);
    }

    @ApiOperation("我的发布列表")
    @GetMapping("/my")
    public Result<Page<TextbookVO>> myPublish(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        Page<TextbookVO> page = textbookService.myPublish(pageNum, pageSize, status);
        return Result.success(page);
    }

    @ApiOperation("上架/下架教材")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        textbookService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("删除教材")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        textbookService.deleteTextbook(id);
        return Result.success();
    }
}
