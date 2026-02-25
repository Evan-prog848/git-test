package com.textbook.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.textbook.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT SUM(price) FROM orders ${ew.customSqlSegment}")
    BigDecimal sumPrice(@Param(Constants.WRAPPER) QueryWrapper<Order> queryWrapper);

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, SUM(price) as value " +
            "FROM orders " +
            "WHERE create_time >= #{startTime} AND status = 2 " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') " +
            "ORDER BY date ASC")
    List<Map<String, Object>> selectTrendData(@Param("startTime") java.time.LocalDateTime startTime);
}
