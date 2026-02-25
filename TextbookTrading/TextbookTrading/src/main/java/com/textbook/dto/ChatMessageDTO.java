package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 聊天消息DTO
 */
@Data
public class ChatMessageDTO {

    /**
     * 接收者ID
     */
    @NotNull(message = "接收者不能为空")
    private Long toUserId;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 消息类型（1文本 2图片）
     */
    private Integer type;
}
