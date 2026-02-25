package com.textbook.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 信用记录VO
 */
@Data
public class CreditRecordVO {

    private Long id;

    private Long userId;

    private String username;

    private String nickname;

    private Long orderId;

    private String orderNo;

    /**
     * 记录类型（1爽约 2实物不符 3好评奖励 4差评扣分 5举报成立）
     */
    private Integer type;

    private String typeName;

    /**
     * 信用分变化
     */
    private Integer scoreChange;

    private String description;

    private Long reporterId;

    private String reporterName;

    /**
     * 被举报人名称
     */
    private String reportedName;

    /**
     * 证据图片URL列表
     */
    private List<String> evidenceImages;

    /**
     * 状态（0待处理 1已处理）
     */
    private Integer status;

    private LocalDateTime createTime;
}
