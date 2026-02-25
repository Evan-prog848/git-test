package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 订单创建DTO
 */
@Data
public class OrderDTO {

    @NotNull(message = "教材ID不能为空")
    private Long textbookId;

    /**
     * 交易点ID
     */
    private Long tradingPointId;

    /**
     * 见面时间
     */
    private LocalDateTime meetingTime;

    private String remark;
}
