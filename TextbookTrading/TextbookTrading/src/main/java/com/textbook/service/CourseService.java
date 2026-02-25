package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Course;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService extends IService<Course> {

    /**
     * 根据专业获取课程列表
     */
    List<Course> listByMajorId(Long majorId);

    /**
     * 根据关键词搜索课程
     */
    List<Course> searchByKeyword(String keyword);

    /**
     * 获取所有启用的课程列表
     */
    List<Course> listEnabled();
}
