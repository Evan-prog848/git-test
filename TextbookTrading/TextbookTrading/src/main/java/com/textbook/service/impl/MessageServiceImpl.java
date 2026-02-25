package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.MessageDTO;
import com.textbook.entity.Message;
import com.textbook.entity.User;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.MessageMapper;
import com.textbook.service.MessageService;
import com.textbook.service.UserService;
import com.textbook.utils.UserContext;
import com.textbook.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息服务实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public void sendMessage(MessageDTO dto) {
        Long userId = UserContext.getUserId();

        if (userId.equals(dto.getToUserId())) {
            throw new BusinessException("不能给自己发送私信");
        }

        // 检查接收者是否存在
        User toUser = userService.getById(dto.getToUserId());
        if (toUser == null) {
            throw new BusinessException("用户不存在");
        }

        Message message = new Message();
        message.setFromUserId(userId);
        message.setToUserId(dto.getToUserId());
        message.setType(Constants.MESSAGE_TYPE_PRIVATE);
        message.setContent(dto.getContent());
        message.setIsRead(Constants.READ_NO);

        this.save(message);
    }

    @Override
    public void sendSystemNotice(Long toUserId, String title, String content) {
        Message message = new Message();
        message.setToUserId(toUserId);
        message.setType(Constants.MESSAGE_TYPE_SYSTEM);
        message.setTitle(title);
        message.setContent(content);
        message.setIsRead(Constants.READ_NO);

        this.save(message);
    }

    @Override
    public Page<MessageVO> pageList(Integer pageNum, Integer pageSize, Integer type) {
        Long userId = UserContext.getUserId();

        Page<Message> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();

        // 查询用户发送和接收的所有私信
        if (type != null && type == Constants.MESSAGE_TYPE_PRIVATE) {
            wrapper.and(w -> w.eq(Message::getToUserId, userId).or().eq(Message::getFromUserId, userId));
            wrapper.eq(Message::getType, type);
        } else if (type != null) {
            // 系统消息只查接收的
            wrapper.eq(Message::getToUserId, userId);
            wrapper.eq(Message::getType, type);
        } else {
            // 不指定类型时，查询接收的所有消息 + 发送的私信
            wrapper.and(w -> w.eq(Message::getToUserId, userId)
                    .or(w2 -> w2.eq(Message::getFromUserId, userId).eq(Message::getType, Constants.MESSAGE_TYPE_PRIVATE)));
        }
        wrapper.orderByDesc(Message::getCreateTime);

        Page<Message> result = this.page(page, wrapper);

        // 转换为VO
        Page<MessageVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<MessageVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public Integer unreadCount() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getToUserId, userId)
               .eq(Message::getIsRead, Constants.READ_NO);
        return Math.toIntExact(this.count(wrapper));
    }

    @Override
    public void markAsRead(Long id) {
        Long userId = UserContext.getUserId();

        Message message = this.getById(id);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        if (!message.getToUserId().equals(userId)) {
            throw new BusinessException("无权操作此消息");
        }

        message.setIsRead(Constants.READ_YES);
        this.updateById(message);
    }

    @Override
    public void markAllAsRead() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getToUserId, userId)
               .eq(Message::getIsRead, Constants.READ_NO);

        Message update = new Message();
        update.setIsRead(Constants.READ_YES);
        this.update(update, wrapper);
    }

    @Override
    public void deleteMessage(Long id) {
        Long userId = UserContext.getUserId();

        Message message = this.getById(id);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        if (!message.getToUserId().equals(userId)) {
            throw new BusinessException("无权删除此消息");
        }

        this.removeById(id);
    }

    private MessageVO convertToVO(Message message) {
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);

        // 获取发送者信息
        if (message.getFromUserId() != null) {
            User fromUser = userService.getById(message.getFromUserId());
            if (fromUser != null) {
                vo.setFromUserName(fromUser.getNickname());
                vo.setFromUserAvatar(fromUser.getAvatar());
            }
        }

        // 获取接收者信息
        if (message.getToUserId() != null) {
            User toUser = userService.getById(message.getToUserId());
            if (toUser != null) {
                vo.setToNickname(toUser.getNickname());
                vo.setToAvatar(toUser.getAvatar());
            }
        }

        return vo;
    }
}
