package com.textbook.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ISBN查询返回的书籍信息VO
 */
@Data
public class BookInfoVO {

    /**
     * ISBN号
     */
    private String isbn;

    /**
     * 书名
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 封面图片URL
     */
    private String cover;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 出版日期
     */
    private String publishDate;

    /**
     * 简介
     */
    private String summary;

    /**
     * 是否查询成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}
