package com.textbook.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息VO
 */
@Data
public class MessageVO {

    private Long id;

    private Long fromUserId;

    private String fromUserName;

    private String fromUserAvatar;

    private Long toUserId;

    private String toNickname;

    private String toAvatar;

    private Integer type;

    private String title;

    private String content;

    private Integer isRead;

    private LocalDateTime createTime;
}
