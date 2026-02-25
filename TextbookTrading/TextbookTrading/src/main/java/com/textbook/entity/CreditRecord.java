package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信用记录实体类
 */
@Data
@TableName("credit_record")
public class CreditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long orderId;

    /**
     * 记录类型（1爽约 2实物不符 3好评奖励 4差评扣分 5举报成立）
     */
    private Integer type;

    /**
     * 信用分变化（正数加分，负数扣分）
     */
    private Integer scoreChange;

    private String description;

    private Long reporterId;

    /**
     * 证据图片URL（JSON数组）
     */
    private String evidence;

    /**
     * 状态（0待处理 1已处理）
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
