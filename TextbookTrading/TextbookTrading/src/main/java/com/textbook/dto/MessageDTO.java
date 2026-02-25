package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 私信DTO
 */
@Data
public class MessageDTO {

    @NotNull(message = "接收者ID不能为空")
    private Long toUserId;

    @NotBlank(message = "消息内容不能为空")
    private String content;
}
