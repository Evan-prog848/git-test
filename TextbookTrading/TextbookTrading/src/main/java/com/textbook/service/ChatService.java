package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.ChatMessage;
import com.textbook.vo.ChatMessageVO;
import com.textbook.vo.ConversationVO;

import java.util.List;

/**
 * 聊天服务接口
 */
public interface ChatService extends IService<ChatMessage> {

    /**
     * 发送消息
     */
    ChatMessage sendMessage(Long fromUserId, Long toUserId, String content, Integer type);

    /**
     * 获取会话列表
     */
    List<ConversationVO> getConversationList(Long userId);

    /**
     * 获取会话消息历史
     */
    Page<ChatMessageVO> getMessageHistory(Long userId, Long otherUserId, Integer pageNum, Integer pageSize);

    /**
     * 标记会话消息为已读
     */
    void markAsRead(Long userId, Long otherUserId);

    /**
     * 获取未读消息数量
     */
    Integer getUnreadCount(Long userId);

    /**
     * 生成会话ID
     */
    String generateConversationId(Long userId1, Long userId2);
}
