package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.ChatMessage;
import com.textbook.entity.User;
import com.textbook.mapper.ChatMessageMapper;
import com.textbook.mapper.UserMapper;
import com.textbook.service.ChatService;
import com.textbook.vo.ChatMessageVO;
import com.textbook.vo.ConversationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 聊天服务实现类
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ChatMessage sendMessage(Long fromUserId, Long toUserId, String content, Integer type) {
        ChatMessage message = new ChatMessage();
        message.setConversationId(generateConversationId(fromUserId, toUserId));
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setContent(content);
        message.setType(type != null ? type : 1);
        message.setIsRead(0);
        save(message);
        return message;
    }

    @Override
    public List<ConversationVO> getConversationList(Long userId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(ChatMessage::getFromUserId, userId)
                        .or().eq(ChatMessage::getToUserId, userId))
                .orderByDesc(ChatMessage::getCreateTime);
        List<ChatMessage> allMessages = list(wrapper);

        Map<String, ConversationVO> conversationMap = new LinkedHashMap<>();

        for (ChatMessage msg : allMessages) {
            String conversationId = msg.getConversationId();
            if (!conversationMap.containsKey(conversationId)) {
                ConversationVO vo = new ConversationVO();
                vo.setConversationId(conversationId);

                Long otherUserId = msg.getFromUserId().equals(userId) ? msg.getToUserId() : msg.getFromUserId();
                vo.setOtherUserId(otherUserId);

                User otherUser = userMapper.selectById(otherUserId);
                if (otherUser != null) {
                    vo.setOtherUserNickname(otherUser.getNickname());
                    vo.setOtherUserAvatar(otherUser.getAvatar());
                }

                vo.setLastMessage(msg.getContent());
                vo.setLastMessageTime(msg.getCreateTime());
                vo.setUnreadCount(0);

                conversationMap.put(conversationId, vo);
            }

            if (msg.getToUserId().equals(userId) && msg.getIsRead() == 0) {
                ConversationVO vo = conversationMap.get(conversationId);
                vo.setUnreadCount(vo.getUnreadCount() + 1);
            }
        }

        return new ArrayList<>(conversationMap.values());
    }

    @Override
    public Page<ChatMessageVO> getMessageHistory(Long userId, Long otherUserId, Integer pageNum, Integer pageSize) {
        String conversationId = generateConversationId(userId, otherUserId);

        Page<ChatMessage> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getConversationId, conversationId)
                .orderByDesc(ChatMessage::getCreateTime);
        page = page(page, wrapper);

        Map<Long, User> userCache = new HashMap<>();

        Page<ChatMessageVO> voPage = new Page<>(pageNum, pageSize, page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(msg -> {
            ChatMessageVO vo = new ChatMessageVO();
            BeanUtils.copyProperties(msg, vo);
            vo.setIsSelf(msg.getFromUserId().equals(userId));

            User fromUser = userCache.computeIfAbsent(msg.getFromUserId(), userMapper::selectById);
            if (fromUser != null) {
                vo.setFromUserNickname(fromUser.getNickname());
                vo.setFromUserAvatar(fromUser.getAvatar());
            }

            User toUser = userCache.computeIfAbsent(msg.getToUserId(), userMapper::selectById);
            if (toUser != null) {
                vo.setToUserNickname(toUser.getNickname());
                vo.setToUserAvatar(toUser.getAvatar());
            }

            return vo;
        }).collect(Collectors.toList()));

        return voPage;
    }

    @Override
    public void markAsRead(Long userId, Long otherUserId) {
        String conversationId = generateConversationId(userId, otherUserId);
        baseMapper.markConversationAsRead(conversationId, userId);
    }

    @Override
    public Integer getUnreadCount(Long userId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getToUserId, userId)
                .eq(ChatMessage::getIsRead, 0);
        return Math.toIntExact(count(wrapper));
    }

    @Override
    public String generateConversationId(Long userId1, Long userId2) {
        long minId = Math.min(userId1, userId2);
        long maxId = Math.max(userId1, userId2);
        return minId + "_" + maxId;
    }
}
