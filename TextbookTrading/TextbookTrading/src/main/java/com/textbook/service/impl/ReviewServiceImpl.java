package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.ReviewDTO;
import com.textbook.entity.Order;
import com.textbook.entity.Review;
import com.textbook.entity.User;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.ReviewMapper;
import com.textbook.service.MessageService;
import com.textbook.service.OrderService;
import com.textbook.service.ReviewService;
import com.textbook.service.UserService;
import com.textbook.utils.UserContext;
import com.textbook.vo.ReviewVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价服务实现类
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    @Lazy
    private OrderService orderService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private MessageService messageService;

    @Override
    public void submitReview(ReviewDTO dto) {
        Long userId = UserContext.getUserId();

        Order order = orderService.getById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != Constants.ORDER_STATUS_COMPLETED) {
            throw new BusinessException("订单未完成，无法评价");
        }

        // 确定被评价人
        Long toUserId;
        if (order.getBuyerId().equals(userId)) {
            toUserId = order.getSellerId();
        } else if (order.getSellerId().equals(userId)) {
            toUserId = order.getBuyerId();
        } else {
            throw new BusinessException("无权评价此订单");
        }

        // 检查是否已评价
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getOrderId, dto.getOrderId())
               .eq(Review::getFromUserId, userId);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("您已评价过此订单");
        }

        Review review = new Review();
        review.setOrderId(dto.getOrderId());
        review.setFromUserId(userId);
        review.setToUserId(toUserId);
        review.setScore(dto.getScore());
        review.setContent(dto.getContent());

        this.save(review);

        // 发送通知
        User fromUser = userService.getById(userId);
        messageService.sendSystemNotice(
            toUserId,
            "收到新评价",
            fromUser.getNickname() + " 对您进行了评价"
        );
    }

    @Override
    public Page<ReviewVO> receivedReviews(Long userId, Integer pageNum, Integer pageSize) {
        Page<Review> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getToUserId, userId)
               .orderByDesc(Review::getCreateTime);

        Page<Review> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    @Override
    public Page<ReviewVO> myReviews(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();

        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getFromUserId, userId)
               .orderByDesc(Review::getCreateTime);

        Page<Review> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    @Override
    public Boolean isReviewed(Long orderId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return false;
        }

        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getOrderId, orderId)
               .eq(Review::getFromUserId, userId);
        return this.count(wrapper) > 0;
    }

    @Override
    public Double getGoodRate(Long userId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getToUserId, userId);

        List<Review> reviews = this.list(wrapper);
        if (reviews.isEmpty()) {
            return 100.0;
        }

        long goodCount = reviews.stream()
                .filter(r -> r.getScore() >= 4)
                .count();
        return (double) goodCount / reviews.size() * 100;
    }

    @Override
    public void deleteReview(Long id) {
        this.removeById(id);
    }

    @Override
    public Page<ReviewVO> adminPageList(Integer pageNum, Integer pageSize, String keyword) {
        Page<Review> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Review::getCreateTime);

        Page<Review> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    private Page<ReviewVO> convertToVOPage(Page<Review> page) {
        Page<ReviewVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<ReviewVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    private ReviewVO convertToVO(Review review) {
        ReviewVO vo = new ReviewVO();
        BeanUtils.copyProperties(review, vo);

        User fromUser = userService.getById(review.getFromUserId());
        if (fromUser != null) {
            vo.setFromUserName(fromUser.getNickname());
            vo.setFromUserAvatar(fromUser.getAvatar());
        }

        return vo;
    }
}
