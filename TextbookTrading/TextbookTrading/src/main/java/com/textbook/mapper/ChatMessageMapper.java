package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 聊天消息Mapper
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 标记会话消息为已读
     */
    @Update("UPDATE chat_message SET is_read = 1 WHERE conversation_id = #{conversationId} AND to_user_id = #{userId} AND is_read = 0")
    int markConversationAsRead(@Param("conversationId") String conversationId, @Param("userId") Long userId);
}
