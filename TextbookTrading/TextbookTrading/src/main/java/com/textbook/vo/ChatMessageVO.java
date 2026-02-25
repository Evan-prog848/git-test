package com.textbook.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息VO
 */
@Data
public class ChatMessageVO {

    private Long id;

    private String conversationId;

    private Long fromUserId;

    private String fromUserNickname;

    private String fromUserAvatar;

    private Long toUserId;

    private String toUserNickname;

    private String toUserAvatar;

    private String content;

    /**
     * 消息类型（1文本 2图片 3系统消息）
     */
    private Integer type;

    /**
     * 是否是自己发送的消息
     */
    private Boolean isSelf;

    private Integer isRead;

    private LocalDateTime createTime;
}
