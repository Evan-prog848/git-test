package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 审核DTO
 */
@Data
public class AuditDTO {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    /**
     * 审核备注（拒绝时的原因）
     */
    private String auditRemark;
}
