package com.textbook.dto;

import lombok.Data;

/**
 * 用户信息更新DTO
 */
@Data
public class UserUpdateDTO {

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private String school;

    private String college;

    private String major;
}
