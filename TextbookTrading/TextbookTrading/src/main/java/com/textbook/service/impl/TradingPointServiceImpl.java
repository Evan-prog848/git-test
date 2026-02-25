package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.TradingPoint;
import com.textbook.mapper.TradingPointMapper;
import com.textbook.service.TradingPointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易点服务实现类
 */
@Service
public class TradingPointServiceImpl extends ServiceImpl<TradingPointMapper, TradingPoint> implements TradingPointService {

    @Override
    public List<TradingPoint> listByCampusId(Long campusId) {
        LambdaQueryWrapper<TradingPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TradingPoint::getStatus, 1);
        if (campusId != null) {
            wrapper.eq(TradingPoint::getCampusId, campusId);
        }
        wrapper.orderByAsc(TradingPoint::getSort, TradingPoint::getId);
        return list(wrapper);
    }

    @Override
    public List<TradingPoint> listEnabled() {
        return listByCampusId(null);
    }
}
