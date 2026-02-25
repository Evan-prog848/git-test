package com.textbook.controller;

import com.textbook.common.Result;
import com.textbook.entity.TradingPoint;
import com.textbook.service.TradingPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交易点控制器
 */
@Api(tags = "交易点模块")
@RestController
@RequestMapping("/api/trading-point")
public class TradingPointController {

    @Autowired
    private TradingPointService tradingPointService;

    @ApiOperation("获取交易点列表")
    @GetMapping("/list")
    public Result<List<TradingPoint>> list(@RequestParam(required = false) Long campusId) {
        List<TradingPoint> list = tradingPointService.listByCampusId(campusId);
        return Result.success(list);
    }
}
