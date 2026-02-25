package com.textbook.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 求购VO
 */
@Data
public class WantedVO {

    private Long id;

    private Long userId;

    private String nickname;

    private String avatar;

    private String title;

    private String author;

    private BigDecimal maxPrice;

    private String description;

    private Integer status;

    private LocalDateTime createTime;
}
