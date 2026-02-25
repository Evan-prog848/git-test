package com.textbook.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话列表VO
 */
@Data
public class ConversationVO {

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 对方用户ID
     */
    private Long otherUserId;

    /**
     * 对方用户昵称
     */
    private String otherUserNickname;

    /**
     * 对方用户头像
     */
    private String otherUserAvatar;

    /**
     * 最后一条消息内容
     */
    private String lastMessage;

    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageTime;

    /**
     * 未读消息数量
     */
    private Integer unreadCount;
}
