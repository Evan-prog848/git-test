package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Campus;
import com.textbook.mapper.CampusMapper;
import com.textbook.service.CampusService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 校区服务实现类
 */
@Service
public class CampusServiceImpl extends ServiceImpl<CampusMapper, Campus> implements CampusService {

    @Override
    public List<Campus> listEnabled() {
        LambdaQueryWrapper<Campus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Campus::getStatus, 1)
                .orderByAsc(Campus::getId);
        return list(wrapper);
    }
}
