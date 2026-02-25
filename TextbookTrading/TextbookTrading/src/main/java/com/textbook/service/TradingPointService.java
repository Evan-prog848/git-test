package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.TradingPoint;

import java.util.List;

/**
 * 交易点服务接口
 */
public interface TradingPointService extends IService<TradingPoint> {

    /**
     * 根据校区获取交易点列表
     */
    List<TradingPoint> listByCampusId(Long campusId);

    /**
     * 获取所有启用的交易点列表
     */
    List<TradingPoint> listEnabled();
}
