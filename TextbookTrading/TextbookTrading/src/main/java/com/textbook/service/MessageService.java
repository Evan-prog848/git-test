package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.MessageDTO;
import com.textbook.entity.Message;
import com.textbook.vo.MessageVO;

/**
 * 消息服务接口
 */
public interface MessageService extends IService<Message> {

    /**
     * 发送私信
     */
    void sendMessage(MessageDTO dto);

    /**
     * 发送系统通知
     */
    void sendSystemNotice(Long toUserId, String title, String content);

    /**
     * 消息列表
     */
    Page<MessageVO> pageList(Integer pageNum, Integer pageSize, Integer type);

    /**
     * 未读消息数量
     */
    Integer unreadCount();

    /**
     * 标记消息为已读
     */
    void markAsRead(Long id);

    /**
     * 标记所有消息为已读
     */
    void markAllAsRead();

    /**
     * 删除消息
     */
    void deleteMessage(Long id);
}
