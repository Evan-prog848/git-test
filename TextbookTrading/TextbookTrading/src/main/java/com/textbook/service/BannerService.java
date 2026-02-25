package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Banner;

import java.util.List;

/**
 * 轮播图服务接口
 */
public interface BannerService extends IService<Banner> {

    /**
     * 获取轮播图列表（前台）
     */
    List<Banner> listEnabled();

    /**
     * 获取所有轮播图（后台）
     */
    List<Banner> listAll();

    /**
     * 添加轮播图
     */
    void addBanner(Banner banner);

    /**
     * 更新轮播图
     */
    void updateBanner(Banner banner);

    /**
     * 删除轮播图
     */
    void deleteBanner(Long id);
}
