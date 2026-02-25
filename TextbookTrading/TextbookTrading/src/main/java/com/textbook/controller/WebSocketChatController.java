package com.textbook.controller;

import com.textbook.dto.ChatMessageDTO;
import com.textbook.entity.ChatMessage;
import com.textbook.service.ChatService;
import com.textbook.vo.ChatMessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket聊天控制器
 * 处理实时消息推送
 */
@Controller
public class WebSocketChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    /**
     * 处理发送消息
     * 客户端发送到 /app/chat.send
     */
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDTO dto, Principal principal) {
        if (principal == null) {
            return;
        }

        Long fromUserId = Long.parseLong(principal.getName());
        
        // 保存消息到数据库
        ChatMessage message = chatService.sendMessage(fromUserId, dto.getToUserId(), dto.getContent(), dto.getType());

        // 构建消息VO
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(message, vo);
        vo.setIsSelf(false);

        // 推送消息给接收者
        messagingTemplate.convertAndSendToUser(
                dto.getToUserId().toString(),
                "/queue/messages",
                vo
        );

        // 同时推送给发送者（确认消息已发送）
        vo.setIsSelf(true);
        messagingTemplate.convertAndSendToUser(
                fromUserId.toString(),
                "/queue/messages",
                vo
        );
    }

    /**
     * 处理用户上线通知
     */
    @MessageMapping("/chat.online")
    public void userOnline(Principal principal) {
        if (principal == null) {
            return;
        }
        // 可以在这里处理用户上线逻辑，如通知好友
    }

    /**
     * 处理消息已读通知
     */
    @MessageMapping("/chat.read")
    public void markAsRead(@Payload Long otherUserId, Principal principal) {
        if (principal == null) {
            return;
        }
        Long userId = Long.parseLong(principal.getName());
        chatService.markAsRead(userId, otherUserId);

        // 通知对方消息已读
        messagingTemplate.convertAndSendToUser(
                otherUserId.toString(),
                "/queue/read",
                userId
        );
    }
}
