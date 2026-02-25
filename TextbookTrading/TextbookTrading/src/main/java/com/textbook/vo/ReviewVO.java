package com.textbook.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价VO
 */
@Data
public class ReviewVO {

    private Long id;

    private Long orderId;

    private Long fromUserId;

    private String fromUserName;

    private String fromUserAvatar;

    private Long toUserId;

    private Integer score;

    private String content;

    private LocalDateTime createTime;
}
