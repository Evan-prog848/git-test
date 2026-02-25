package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.service.OrderService;
import com.textbook.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台订单管理控制器
 */
@Api(tags = "后台-订单管理模块")
@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("订单列表")
    @GetMapping("/list")
    public Result<Page<OrderVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        Page<OrderVO> page = orderService.adminPageList(pageNum, pageSize, keyword, status);
        return Result.success(page);
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        OrderVO vo = orderService.getDetail(id);
        return Result.success(vo);
    }
}
