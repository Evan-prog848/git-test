package com.textbook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.OrderDTO;
import com.textbook.entity.Order;
import com.textbook.entity.Textbook;
import com.textbook.entity.TradingPoint;
import com.textbook.entity.User;
import com.textbook.mapper.TradingPointMapper;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.OrderMapper;
import com.textbook.service.*;
import com.textbook.utils.UserContext;
import com.textbook.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    @Lazy
    private TextbookService textbookService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private TradingPointMapper tradingPointMapper;

    @Autowired
    @Lazy
    private MessageService messageService;

    @Autowired
    @Lazy
    private ReviewService reviewService;

    @Override
    @Transactional
    public String createOrder(OrderDTO dto) {
        Long buyerId = UserContext.getUserId();

        Textbook textbook = textbookService.getById(dto.getTextbookId());
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }
        if (textbook.getStatus() != Constants.TEXTBOOK_STATUS_ON) {
            throw new BusinessException("教材已下架或已售出");
        }
        if (textbook.getUserId().equals(buyerId)) {
            throw new BusinessException("不能购买自己发布的教材");
        }

        // 检查是否已有待处理订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getTextbookId, dto.getTextbookId())
               .eq(Order::getBuyerId, buyerId)
               .in(Order::getStatus, Constants.ORDER_STATUS_PENDING, Constants.ORDER_STATUS_CONFIRMED);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("您已对此教材发起过购买申请");
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setTextbookId(dto.getTextbookId());
        order.setSellerId(textbook.getUserId());
        order.setBuyerId(buyerId);
        order.setPrice(textbook.getPrice());
        order.setTradingPointId(dto.getTradingPointId());
        order.setMeetingTime(dto.getMeetingTime());
        order.setRemark(dto.getRemark());
        order.setStatus(Constants.ORDER_STATUS_PENDING);

        this.save(order);

        // 发送通知给卖家
        User buyer = userService.getById(buyerId);
        messageService.sendSystemNotice(
            textbook.getUserId(),
            "新的购买申请",
            buyer.getNickname() + " 想要购买您的教材《" + textbook.getTitle() + "》"
        );

        return order.getOrderNo();
    }

    @Override
    public OrderVO getDetail(Long id) {
        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        Long userId = UserContext.getUserId();
        if (!order.getBuyerId().equals(userId) && !order.getSellerId().equals(userId)) {
            throw new BusinessException("无权查看此订单");
        }

        return convertToVO(order);
    }

    @Override
    public Page<OrderVO> buyerOrders(Integer pageNum, Integer pageSize, Integer status) {
        Long userId = UserContext.getUserId();

        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBuyerId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    @Override
    public Page<OrderVO> sellerOrders(Integer pageNum, Integer pageSize, Integer status) {
        Long userId = UserContext.getUserId();

        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getSellerId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    @Override
    @Transactional
    public void confirmOrder(Long id) {
        Long userId = UserContext.getUserId();

        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != Constants.ORDER_STATUS_PENDING) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(Constants.ORDER_STATUS_CONFIRMED);
        this.updateById(order);

        // 通知买家
        Textbook textbook = textbookService.getById(order.getTextbookId());
        messageService.sendSystemNotice(
            order.getBuyerId(),
            "订单已确认",
            "您购买《" + textbook.getTitle() + "》的订单已被卖家确认"
        );
    }

    @Override
    @Transactional
    public void rejectOrder(Long id) {
        Long userId = UserContext.getUserId();

        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != Constants.ORDER_STATUS_PENDING) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        this.updateById(order);

        // 通知买家
        Textbook textbook = textbookService.getById(order.getTextbookId());
        messageService.sendSystemNotice(
            order.getBuyerId(),
            "订单已拒绝",
            "很遗憾，您购买《" + textbook.getTitle() + "》的订单已被卖家拒绝"
        );
    }

    @Override
    @Transactional
    public void completeOrder(Long id) {
        Long userId = UserContext.getUserId();

        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() != Constants.ORDER_STATUS_CONFIRMED) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(Constants.ORDER_STATUS_COMPLETED);
        this.updateById(order);

        // 更新教材状态为已售
        Textbook textbook = textbookService.getById(order.getTextbookId());
        textbook.setStatus(Constants.TEXTBOOK_STATUS_SOLD);
        textbookService.updateById(textbook);

        // 通知卖家
        messageService.sendSystemNotice(
            order.getSellerId(),
            "交易完成",
            "《" + textbook.getTitle() + "》的交易已完成，请前往评价"
        );
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Long userId = UserContext.getUserId();

        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getBuyerId().equals(userId) && !order.getSellerId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getStatus() == Constants.ORDER_STATUS_COMPLETED ||
            order.getStatus() == Constants.ORDER_STATUS_CANCELLED) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        this.updateById(order);
    }

    @Override
    public Page<OrderVO> adminPageList(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Order::getOrderNo, keyword);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    private Page<OrderVO> convertToVOPage(Page<Order> page) {
        Page<OrderVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<OrderVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        // 获取教材信息
        Textbook textbook = textbookService.getById(order.getTextbookId());
        if (textbook != null) {
            vo.setTextbookTitle(textbook.getTitle());
            vo.setTextbookCover(textbook.getCover());
        }

        // 获取卖家信息
        User seller = userService.getById(order.getSellerId());
        if (seller != null) {
            vo.setSellerName(seller.getNickname());
            vo.setSellerAvatar(seller.getAvatar());
        }

        // 获取买家信息
        User buyer = userService.getById(order.getBuyerId());
        if (buyer != null) {
            vo.setBuyerName(buyer.getNickname());
            vo.setBuyerAvatar(buyer.getAvatar());
        }

        // 获取交易点信息
        if (order.getTradingPointId() != null) {
            TradingPoint tradingPoint = tradingPointMapper.selectById(order.getTradingPointId());
            if (tradingPoint != null) {
                vo.setTradingPointName(tradingPoint.getName());
            }
        }
        vo.setMeetingTime(order.getMeetingTime());

        // 订单状态文字
        vo.setStatusText(getStatusText(order.getStatus()));

        // 是否已评价
        vo.setIsReviewed(reviewService.isReviewed(order.getId()));

        return vo;
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待确认";
            case 1: return "已确认";
            case 2: return "已完成";
            case 3: return "已取消";
            default: return "未知";
        }
    }
}
