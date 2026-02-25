package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
@Data
@TableName("chat_message")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID（由两个用户ID组合生成）
     */
    private String conversationId;

    private Long fromUserId;

    private Long toUserId;

    private String content;

    /**
     * 消息类型（1文本 2图片 3系统消息）
     */
    private Integer type;

    /**
     * 是否已读（0未读 1已读）
     */
    private Integer isRead;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
