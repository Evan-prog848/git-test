package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.OrderDTO;
import com.textbook.service.OrderService;
import com.textbook.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@Api(tags = "订单模块")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    public Result<String> create(@RequestBody OrderDTO dto) {
        String orderNo = orderService.createOrder(dto);
        return Result.success(orderNo);
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        OrderVO vo = orderService.getDetail(id);
        return Result.success(vo);
    }

    @ApiOperation("我买的订单列表")
    @GetMapping("/buyer")
    public Result<Page<OrderVO>> buyerOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        Page<OrderVO> page = orderService.buyerOrders(pageNum, pageSize, status);
        return Result.success(page);
    }

    @ApiOperation("我卖的订单列表")
    @GetMapping("/seller")
    public Result<Page<OrderVO>> sellerOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        Page<OrderVO> page = orderService.sellerOrders(pageNum, pageSize, status);
        return Result.success(page);
    }

    @ApiOperation("卖家确认订单")
    @PutMapping("/confirm/{id}")
    public Result<Void> confirm(@PathVariable Long id) {
        orderService.confirmOrder(id);
        return Result.success();
    }

    @ApiOperation("卖家拒绝订单")
    @PutMapping("/reject/{id}")
    public Result<Void> reject(@PathVariable Long id) {
        orderService.rejectOrder(id);
        return Result.success();
    }

    @ApiOperation("买家确认收货")
    @PutMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }
}
