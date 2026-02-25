package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.Notice;
import com.textbook.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台公告管理控制器
 */
@Api(tags = "后台-公告管理模块")
@RestController
@RequestMapping("/api/admin/notice")
public class AdminNoticeController {

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

    @ApiOperation("添加公告")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Notice notice) {
        noticeService.addNotice(notice);
        return Result.success();
    }

    @ApiOperation("更新公告")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Notice notice) {
        noticeService.updateNotice(notice);
        return Result.success();
    }

    @ApiOperation("删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success();
    }
}
