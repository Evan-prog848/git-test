package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关注Mapper接口
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
}
