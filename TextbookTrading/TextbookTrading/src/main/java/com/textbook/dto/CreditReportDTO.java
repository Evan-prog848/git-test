package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 信用举报DTO
 */
@Data
public class CreditReportDTO {

    /**
     * 被举报用户ID
     */
    @NotNull(message = "被举报用户不能为空")
    private Long userId;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 举报类型（1爽约 2实物不符）
     */
    @NotNull(message = "举报类型不能为空")
    private Integer type;

    /**
     * 举报描述
     */
    private String description;

    /**
     * 证据图片URL列表
     */
    private List<String> evidenceImages;
}
