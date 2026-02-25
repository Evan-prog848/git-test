package com.textbook.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 求购发布DTO
 */
@Data
public class WantedDTO {

    private Long id;

    @NotBlank(message = "书名不能为空")
    private String title;

    private String author;

    private BigDecimal maxPrice;

    private String description;
}
