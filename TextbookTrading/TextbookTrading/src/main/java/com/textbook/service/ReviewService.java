package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.ReviewDTO;
import com.textbook.entity.Review;
import com.textbook.vo.ReviewVO;

/**
 * 评价服务接口
 */
public interface ReviewService extends IService<Review> {

    /**
     * 提交评价
     */
    void submitReview(ReviewDTO dto);

    /**
     * 用户收到的评价列表
     */
    Page<ReviewVO> receivedReviews(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 我发出的评价列表
     */
    Page<ReviewVO> myReviews(Integer pageNum, Integer pageSize);

    /**
     * 检查订单是否已评价
     */
    Boolean isReviewed(Long orderId);

    /**
     * 获取用户好评率
     */
    Double getGoodRate(Long userId);

    /**
     * 删除评价（后台）
     */
    void deleteReview(Long id);

    /**
     * 分页查询评价列表（后台）
     */
    Page<ReviewVO> adminPageList(Integer pageNum, Integer pageSize, String keyword);
}
