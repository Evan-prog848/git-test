package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Campus;

import java.util.List;

/**
 * 校区服务接口
 */
public interface CampusService extends IService<Campus> {

    /**
     * 获取所有启用的校区列表
     */
    List<Campus> listEnabled();
}
