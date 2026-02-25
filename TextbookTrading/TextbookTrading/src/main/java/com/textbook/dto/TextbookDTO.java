package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 教材发布DTO
 */
@Data
public class TextbookDTO {

    private Long id;

    private Long categoryId;

    @NotBlank(message = "书名不能为空")
    private String title;

    private String author;

    private String publisher;

    private String isbn;

    private BigDecimal originalPrice;

    @NotNull(message = "售价不能为空")
    @DecimalMin(value = "0.01", message = "售价必须大于0")
    private BigDecimal price;

    private Integer conditionLevel;

    private String course;

    private String description;

    private String cover;

    /**
     * 教材图片列表
     */
    private List<String> images;

    /**
     * 校区ID
     */
    private Long campusId;

    /**
     * 关联课程ID
     */
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
}
