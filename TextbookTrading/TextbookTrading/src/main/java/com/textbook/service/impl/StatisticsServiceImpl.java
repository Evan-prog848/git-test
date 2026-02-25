package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.textbook.common.Constants;
import com.textbook.entity.Order;
import com.textbook.entity.Textbook;
import com.textbook.entity.User;
import com.textbook.mapper.OrderMapper;
import com.textbook.mapper.TextbookMapper;
import com.textbook.mapper.UserMapper;
import com.textbook.service.StatisticsService;
import com.textbook.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TextbookMapper textbookMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public StatisticsVO getStatistics(Integer days) {
        if (days == null) days = 7;
        StatisticsVO vo = new StatisticsVO();

        // 核心数量统计
        vo.setUserCount(userMapper.selectCount(null));
        vo.setTextbookCount(textbookMapper.selectCount(null));
        vo.setOrderCount(orderMapper.selectCount(null));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();

        // 今日新增统计
        vo.setTodayUserCount(userMapper.selectCount(
                new LambdaQueryWrapper<User>().ge(User::getCreateTime, startOfDay)));
        vo.setTodayTextbookCount(textbookMapper.selectCount(
                new LambdaQueryWrapper<Textbook>().ge(Textbook::getCreateTime, startOfDay)));
        vo.setTodayOrderCount(orderMapper.selectCount(
                new LambdaQueryWrapper<Order>().ge(Order::getCreateTime, startOfDay)));

        // 订单和审核统计
        vo.setCompletedOrderCount(orderMapper.selectCount(
                new LambdaQueryWrapper<Order>().eq(Order::getStatus, Constants.ORDER_STATUS_COMPLETED)));
        vo.setPendingAuditCount(textbookMapper.selectCount(
                new LambdaQueryWrapper<Textbook>().eq(Textbook::getAuditStatus, Constants.AUDIT_STATUS_PENDING)));

        // 交易额统计
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Order> todayAmountWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        todayAmountWrapper.ge("create_time", startOfDay).eq("status", Constants.ORDER_STATUS_COMPLETED);
        BigDecimal todayAmount = orderMapper.sumPrice(todayAmountWrapper);
        vo.setTodayAmount(todayAmount != null ? todayAmount : BigDecimal.ZERO);

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Order> totalAmountWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        totalAmountWrapper.eq("status", Constants.ORDER_STATUS_COMPLETED);
        BigDecimal totalAmount = orderMapper.sumPrice(totalAmountWrapper);
        vo.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);

        // 教材分类统计
        List<Map<String, Object>> categoryStats = textbookMapper.selectCategoryStats();
        if (categoryStats != null) {
            vo.setCategoryStats(categoryStats.stream().map(m -> {
                StatisticsVO.CategoryStatVO stat = new StatisticsVO.CategoryStatVO();
                stat.setName(m.get("name") != null ? m.get("name").toString() : "未分类");
                Object val = m.get("value");
                stat.setValue(val != null ? ((Number) val).intValue() : 0);
                return stat;
            }).collect(Collectors.toList()));
        } else {
            vo.setCategoryStats(new ArrayList<>());
        }

        // 趋势数据 (根据days参数获取)
        LocalDateTime trendStartDate = startOfDay.minusDays(days - 1);
        List<Map<String, Object>> trendRawData = orderMapper.selectTrendData(trendStartDate);
        List<StatisticsVO.TrendVO> trendList = new ArrayList<>();
        if (trendRawData == null) trendRawData = new ArrayList<>();
        List<Map<String, Object>> finalTrendRawData = trendRawData;
        for (int i = 0; i < days; i++) {
            String dateStr = now.toLocalDate().minusDays(days - 1 - i).toString();
            StatisticsVO.TrendVO trend = new StatisticsVO.TrendVO();
            trend.setDate(dateStr);
            BigDecimal value = finalTrendRawData.stream()
                    .filter(m -> m.get("date") != null && dateStr.equals(m.get("date").toString()))
                    .map(m -> {
                        Object v = m.get("value");
                        if (v == null) return BigDecimal.ZERO;
                        if (v instanceof BigDecimal) return (BigDecimal) v;
                        return new BigDecimal(v.toString());
                    })
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
            trend.setValue(value);
            trendList.add(trend);
        }
        vo.setTrendData(trendList);

        // 最近活动 (模拟数据，实际可从日志表或各业务表获取)
        List<StatisticsVO.ActivityVO> activities = new ArrayList<>();
        // 示例：获取最近5个教材发布
        List<Textbook> latestTextbooks = textbookMapper.selectList(
                new LambdaQueryWrapper<Textbook>().orderByDesc(Textbook::getCreateTime).last("LIMIT 5"));
        latestTextbooks.forEach(t -> {
            StatisticsVO.ActivityVO activity = new StatisticsVO.ActivityVO();
            activity.setContent("发布了新教材《" + t.getTitle() + "》");
            activity.setTime("刚才");
            activity.setColor("#409eff");
            activities.add(activity);
        });
        vo.setRecentActivities(activities);

        return vo;
    }
}
