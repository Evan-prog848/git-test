package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.CreditReportDTO;
import com.textbook.service.CreditService;
import com.textbook.utils.UserContext;
import com.textbook.vo.CreditRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 信用控制器
 */
@Api(tags = "信用模块")
@RestController
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @ApiOperation("获取当前用户信用分")
    @GetMapping("/score")
    public Result<Integer> getMyScore() {
        Long userId = UserContext.getUserId();
        Integer score = creditService.getUserCreditScore(userId);
        return Result.success(score);
    }

    @ApiOperation("获取指定用户信用分")
    @GetMapping("/score/{userId}")
    public Result<Integer> getUserScore(@PathVariable Long userId) {
        Integer score = creditService.getUserCreditScore(userId);
        return Result.success(score);
    }

    @ApiOperation("获取当前用户信用记录")
    @GetMapping("/records")
    public Result<Page<CreditRecordVO>> getMyRecords(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContext.getUserId();
        Page<CreditRecordVO> page = creditService.getUserCreditRecords(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("举报用户")
    @PostMapping("/report")
    public Result<Void> reportUser(@Valid @RequestBody CreditReportDTO dto) {
        creditService.reportUser(dto);
        return Result.success();
    }
}
