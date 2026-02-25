package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.Notice;
import com.textbook.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公告控制器
 */
@Api(tags = "公告模块")
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation("公告列表")
    @GetMapping("/list")
    public Result<Page<Notice>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Notice> page = noticeService.pageList(pageNum, pageSize);
        return Result.success(page);
    }
}
