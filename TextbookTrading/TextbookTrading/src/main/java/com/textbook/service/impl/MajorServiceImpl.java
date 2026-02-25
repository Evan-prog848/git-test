package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Major;
import com.textbook.mapper.MajorMapper;
import com.textbook.service.MajorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专业服务实现类
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {

    @Override
    public List<Major> listByCampusId(Long campusId) {
        LambdaQueryWrapper<Major> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Major::getStatus, 1);
        if (campusId != null) {
            wrapper.eq(Major::getCampusId, campusId);
        }
        wrapper.orderByAsc(Major::getId);
        return list(wrapper);
    }

    @Override
    public List<Major> listEnabled() {
        return listByCampusId(null);
    }
}
