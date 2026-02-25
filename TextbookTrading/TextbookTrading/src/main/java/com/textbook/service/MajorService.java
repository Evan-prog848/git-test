package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Major;

import java.util.List;

/**
 * 专业服务接口
 */
public interface MajorService extends IService<Major> {

    /**
     * 根据校区获取专业列表
     */
    List<Major> listByCampusId(Long campusId);

    /**
     * 获取所有启用的专业列表
     */
    List<Major> listEnabled();
}
