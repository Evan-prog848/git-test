package com.textbook.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.TradingPoint;
import com.textbook.service.TradingPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台 - 交易点管理控制器
 */
@Api(tags = "管理后台-交易点管理")
@RestController
@RequestMapping("/api/admin/trading-point")
public class AdminTradingPointController {

    @Autowired
    private TradingPointService tradingPointService;

    @ApiOperation("交易点列表")
    @GetMapping("/list")
    public Result<Page<TradingPoint>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long campusId,
            @RequestParam(required = false) String keyword) {
        Page<TradingPoint> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TradingPoint> wrapper = new LambdaQueryWrapper<>();
        if (campusId != null) {
            wrapper.eq(TradingPoint::getCampusId, campusId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(TradingPoint::getName, keyword);
        }
        wrapper.orderByAsc(TradingPoint::getSort, TradingPoint::getId);
        page = tradingPointService.page(page, wrapper);
        return Result.success(page);
    }

    @ApiOperation("添加交易点")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody TradingPoint tradingPoint) {
        tradingPointService.save(tradingPoint);
        return Result.success();
    }

    @ApiOperation("更新交易点")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody TradingPoint tradingPoint) {
        tradingPointService.updateById(tradingPoint);
        return Result.success();
    }

    @ApiOperation("删除交易点")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tradingPointService.removeById(id);
        return Result.success();
    }

    @ApiOperation("更新交易点状态")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        TradingPoint point = new TradingPoint();
        point.setId(id);
        point.setStatus(status);
        tradingPointService.updateById(point);
        return Result.success();
    }
}
