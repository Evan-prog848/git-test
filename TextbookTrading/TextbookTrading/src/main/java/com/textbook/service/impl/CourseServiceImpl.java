package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Course;
import com.textbook.mapper.CourseMapper;
import com.textbook.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public List<Course> listByMajorId(Long majorId) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getStatus, 1);
        if (majorId != null) {
            wrapper.eq(Course::getMajorId, majorId);
        }
        wrapper.orderByAsc(Course::getSemester, Course::getId);
        return list(wrapper);
    }

    @Override
    public List<Course> searchByKeyword(String keyword) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getStatus, 1);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Course::getName, keyword)
                    .or().like(Course::getCode, keyword));
        }
        wrapper.orderByAsc(Course::getId);
        return list(wrapper);
    }

    @Override
    public List<Course> listEnabled() {
        return listByMajorId(null);
    }
}
