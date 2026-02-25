package com.textbook.service;

import com.textbook.vo.StatisticsVO;

/**
 * 统计服务接口
 */
public interface StatisticsService {

    /**
     * 获取统计数据
     * @param days 趋势数据天数 (7戦30)
     */
    StatisticsVO getStatistics(Integer days);
}
