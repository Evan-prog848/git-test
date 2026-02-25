package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.service.CreditService;
import com.textbook.vo.CreditRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台 - 信用管理控制器
 */
@Api(tags = "管理后台-信用管理")
@RestController
@RequestMapping("/api/admin/credit")
public class AdminCreditController {

    @Autowired
    private CreditService creditService;

    @ApiOperation("待处理举报列表")
    @GetMapping("/pending")
    public Result<Page<CreditRecordVO>> pendingList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CreditRecordVO> page = creditService.getPendingReports(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("处理举报")
    @PostMapping("/handle/{id}")
    public Result<Void> handleReport(
            @PathVariable Long id,
            @RequestParam Boolean approved,
            @RequestParam(required = false) String remark) {
        creditService.handleReport(id, approved, remark);
        return Result.success();
    }

    @ApiOperation("查看用户信用记录")
    @GetMapping("/user/{userId}")
    public Result<Page<CreditRecordVO>> userRecords(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CreditRecordVO> page = creditService.getUserCreditRecords(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取用户信用分")
    @GetMapping("/score/{userId}")
    public Result<Integer> getUserScore(@PathVariable Long userId) {
        Integer score = creditService.getUserCreditScore(userId);
        return Result.success(score);
    }

    @ApiOperation("已处理举报列表")
    @GetMapping("/processed")
    public Result<Page<CreditRecordVO>> processedList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CreditRecordVO> page = creditService.getProcessedReports(pageNum, pageSize);
        return Result.success(page);
    }
}
