package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.TextbookImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 教材图片Mapper接口
 */
@Mapper
public interface TextbookImageMapper extends BaseMapper<TextbookImage> {
}
