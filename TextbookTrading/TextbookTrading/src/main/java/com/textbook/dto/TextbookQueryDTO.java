package com.textbook.dto;

import lombok.Data;

/**
 * 教材查询DTO
 */
@Data
public class TextbookQueryDTO {

    /**
     * 关键词（书名、作者、课程）
     */
    private String keyword;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 最低价格
     */
    private Integer minPrice;

    /**
     * 最高价格
     */
    private Integer maxPrice;

    /**
     * 最低新旧程度
     */
    private Integer minCondition;

    /**
     * 校区ID
     */
    private Long campusId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称（模糊查询）
     */
    private String courseName;

    /**
     * 排序方式: latest-最新, price_asc-价格升序, price_desc-价格降序, views-浏览量
     */
    private String orderBy;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;
}
