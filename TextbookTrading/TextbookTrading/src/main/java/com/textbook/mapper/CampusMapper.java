package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.Campus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 校区Mapper
 */
@Mapper
public interface CampusMapper extends BaseMapper<Campus> {
}
