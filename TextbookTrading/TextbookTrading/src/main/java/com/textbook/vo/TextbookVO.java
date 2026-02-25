package com.textbook.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 教材详情VO
 */
@Data
public class TextbookVO {

    private Long id;

    private Long userId;

    private Long categoryId;

    private String categoryName;

    private String title;

    private String author;

    private String publisher;

    private String isbn;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private Integer conditionLevel;

    private String course;

    private String description;

    private String cover;

    private Integer viewCount;

    private Integer status;

    private Integer auditStatus;

    private LocalDateTime createTime;

    /**
     * 教材图片列表
     */
    private List<String> images;

    /**
     * 发布者信息
     */
    private UserVO seller;

    /**
     * 发布者昵称（用于列表显示）
     */
    private String sellerName;

    /**
     * 发布者头像（用于列表显示）
     */
    private String sellerAvatar;

    /**
     * 发布者电话（用于管理后台）
     */
    private String sellerPhone;

    /**
     * 是否已收藏
     */
    private Boolean isFavorite;

    /**
     * 收藏数量
     */
    private Integer favoriteCount;
}
