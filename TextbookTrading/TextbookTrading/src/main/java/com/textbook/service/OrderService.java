package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.OrderDTO;
import com.textbook.entity.Order;
import com.textbook.vo.OrderVO;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     */
    String createOrder(OrderDTO dto);

    /**
     * 获取订单详情
     */
    OrderVO getDetail(Long id);

    /**
     * 我买的订单列表
     */
    Page<OrderVO> buyerOrders(Integer pageNum, Integer pageSize, Integer status);

    /**
     * 我卖的订单列表
     */
    Page<OrderVO> sellerOrders(Integer pageNum, Integer pageSize, Integer status);

    /**
     * 卖家确认订单
     */
    void confirmOrder(Long id);

    /**
     * 卖家拒绝订单
     */
    void rejectOrder(Long id);

    /**
     * 买家确认收货
     */
    void completeOrder(Long id);

    /**
     * 取消订单
     */
    void cancelOrder(Long id);

    /**
     * 分页查询订单列表（后台）
     */
    Page<OrderVO> adminPageList(Integer pageNum, Integer pageSize, String keyword, Integer status);
}
