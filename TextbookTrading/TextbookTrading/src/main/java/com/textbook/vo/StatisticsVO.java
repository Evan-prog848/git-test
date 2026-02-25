package com.textbook.vo;

import lombok.Data;

/**
 * 数据统计VO
 */
@Data
public class StatisticsVO {

    /**
     * 用户总数
     */
    private Long userCount;

    /**
     * 教材总数
     */
    private Long textbookCount;

    /**
     * 订单总数
     */
    private Long orderCount;

    /**
     * 今日新增用户
     */
    private Long todayUserCount;

    /**
     * 今日新增教材
     */
    private Long todayTextbookCount;

    /**
     * 今日新增订单
     */
    private Long todayOrderCount;

    /**
     * 已完成订单数
     */
    private Long completedOrderCount;

    /**
     * 待审核教材数
     */
    private Long pendingAuditCount;

    /**
     * 今日交易额
     */
    private java.math.BigDecimal todayAmount;

    /**
     * 累计交易额
     */
    private java.math.BigDecimal totalAmount;

    /**
     * 教材分类统计
     */
    private java.util.List<CategoryStatVO> categoryStats;

    /**
     * 趋势数据 (近7天或30天)
     */
    private java.util.List<TrendVO> trendData;

    /**
     * 最近活动
     */
    private java.util.List<ActivityVO> recentActivities;

    @Data
    public static class CategoryStatVO {
        private String name;
        private Integer value;
    }

    @Data
    public static class TrendVO {
        private String date;
        private java.math.BigDecimal value;
    }

    @Data
    public static class ActivityVO {
        private String content;
        private String time;
        private String color;
    }
}

