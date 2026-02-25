package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 教材实体类
 */
@Data
@TableName("textbook")
public class Textbook implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long categoryId;

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

    private Long campusId;

    private Long courseId;

    /**
     * 有无笔记（0无 1有）
     */
    private Integer hasNotes;

    /**
     * 有无划线（0无 1有）
     */
    private Integer hasHighlight;

    /**
     * 是否缺页（0否 1是）
     */
    private Integer hasMissingPages;

    /**
     * 有无水渍（0无 1有）
     */
    private Integer hasWaterStains;

    /**
     * 录入方式（0手动 1ISBN扫描）
     */
    private Integer inputMode;

    private Integer viewCount;

    private Integer status;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer auditStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
