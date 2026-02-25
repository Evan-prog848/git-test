package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.service.ReviewService;
import com.textbook.vo.ReviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评价管理控制器
 */
@Api(tags = "后台-评价管理模块")
@RestController
@RequestMapping("/api/admin/review")
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation("评价列表")
    @GetMapping("/list")
    public Result<Page<ReviewVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<ReviewVO> page = reviewService.adminPageList(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @ApiOperation("删除评价")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return Result.success();
    }
}
