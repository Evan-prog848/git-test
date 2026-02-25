package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.ChatMessageDTO;
import com.textbook.entity.ChatMessage;
import com.textbook.service.ChatService;
import com.textbook.utils.UserContext;
import com.textbook.vo.ChatMessageVO;
import com.textbook.vo.ConversationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 聊天控制器（HTTP接口，用于消息持久化和历史记录）
 */
@Api(tags = "聊天模块")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public Result<ChatMessage> sendMessage(@Valid @RequestBody ChatMessageDTO dto) {
        Long fromUserId = UserContext.getUserId();
        ChatMessage message = chatService.sendMessage(fromUserId, dto.getToUserId(), dto.getContent(), dto.getType());
        return Result.success(message);
    }

    @ApiOperation("获取会话列表")
    @GetMapping("/conversations")
    public Result<List<ConversationVO>> getConversations() {
        Long userId = UserContext.getUserId();
        List<ConversationVO> list = chatService.getConversationList(userId);
        return Result.success(list);
    }

    @ApiOperation("获取与某用户的聊天记录")
    @GetMapping("/history/{otherUserId}")
    public Result<Page<ChatMessageVO>> getHistory(
            @PathVariable Long otherUserId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = UserContext.getUserId();
        Page<ChatMessageVO> page = chatService.getMessageHistory(userId, otherUserId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("标记会话消息为已读")
    @PutMapping("/read/{otherUserId}")
    public Result<Void> markAsRead(@PathVariable Long otherUserId) {
        Long userId = UserContext.getUserId();
        chatService.markAsRead(userId, otherUserId);
        return Result.success();
    }

    @ApiOperation("获取未读消息数量")
    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount() {
        Long userId = UserContext.getUserId();
        Integer count = chatService.getUnreadCount(userId);
        return Result.success(count);
    }
}
