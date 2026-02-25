package com.textbook.vo;

import lombok.Data;

/**
 * 登录返回VO
 */
@Data
public class LoginVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private Integer isVerified;

    private String token;

    /**
     * 用户类型（user/admin）
     */
    private String userType;
}
