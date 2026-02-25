package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告Mapper接口
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
