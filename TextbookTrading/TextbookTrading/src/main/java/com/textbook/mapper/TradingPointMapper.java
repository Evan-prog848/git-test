package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.TradingPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易点Mapper
 */
@Mapper
public interface TradingPointMapper extends BaseMapper<TradingPoint> {
}
