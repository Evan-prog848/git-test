package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.ReviewDTO;
import com.textbook.service.ReviewService;
import com.textbook.vo.ReviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 评价控制器
 */
@Api(tags = "评价模块")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation("提交评价")
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody ReviewDTO dto) {
        reviewService.submitReview(dto);
        return Result.success();
    }

    @ApiOperation("用户收到的评价列表")
    @GetMapping("/received/{userId}")
    public Result<Page<ReviewVO>> receivedReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ReviewVO> page = reviewService.receivedReviews(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("我发出的评价列表")
    @GetMapping("/my")
    public Result<Page<ReviewVO>> myReviews(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ReviewVO> page = reviewService.myReviews(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("检查订单是否已评价")
    @GetMapping("/check/{orderId}")
    public Result<Boolean> check(@PathVariable Long orderId) {
        Boolean isReviewed = reviewService.isReviewed(orderId);
        return Result.success(isReviewed);
    }

    @ApiOperation("获取用户好评率")
    @GetMapping("/rate/{userId}")
    public Result<Double> getGoodRate(@PathVariable Long userId) {
        Double rate = reviewService.getGoodRate(userId);
        return Result.success(rate);
    }
}
