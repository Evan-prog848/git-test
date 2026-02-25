package com.textbook.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 */
@Data
public class OrderVO {

    private Long id;

    private String orderNo;

    private Long textbookId;

    private String textbookTitle;

    private String textbookCover;

    private Long sellerId;

    private String sellerName;

    private String sellerAvatar;

    private Long buyerId;

    private String buyerName;

    private String buyerAvatar;

    private BigDecimal price;

    private String remark;

    /**
     * 交易点ID
     */
    private Long tradingPointId;

    /**
     * 交易点名称
     */
    private String tradingPointName;

    /**
     * 见面时间
     */
    private LocalDateTime meetingTime;

    private Integer status;

    private String statusText;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 是否已评价
     */
    private Boolean isReviewed;
}
