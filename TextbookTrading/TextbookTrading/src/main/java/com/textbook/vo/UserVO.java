package com.textbook.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private String school;

    private String college;

    private String major;

    private Integer isVerified;

    private LocalDateTime createTime;

    /**
     * 发布的教材数量
     */
    private Integer textbookCount;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fansCount;

    /**
     * 交易订单数
     */
    private Long orderCount;

    /**
     * 收藏数
     */
    private Long favoriteCount;

    /**
     * 好评率
     */
    private Double goodRate;
}
