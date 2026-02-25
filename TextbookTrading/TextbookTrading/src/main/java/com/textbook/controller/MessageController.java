package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.MessageDTO;
import com.textbook.service.MessageService;
import com.textbook.vo.MessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息控制器
 */
@Api(tags = "消息模块")
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("发送私信")
    @PostMapping("/send")
    public Result<Void> send(@RequestBody MessageDTO dto) {
        messageService.sendMessage(dto);
        return Result.success();
    }

    @ApiOperation("消息列表")
    @GetMapping("/list")
    public Result<Page<MessageVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type) {
        Page<MessageVO> page = messageService.pageList(pageNum, pageSize, type);
        return Result.success(page);
    }

    @ApiOperation("未读消息数量")
    @GetMapping("/unread/count")
    public Result<Integer> unreadCount() {
        Integer count = messageService.unreadCount();
        return Result.success(count);
    }

    @ApiOperation("标记消息为已读")
    @PutMapping("/read/{id}")
    public Result<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.success();
    }

    @ApiOperation("标记所有消息为已读")
    @PutMapping("/read/all")
    public Result<Void> markAllAsRead() {
        messageService.markAllAsRead();
        return Result.success();
    }

    @ApiOperation("删除消息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return Result.success();
    }
}
