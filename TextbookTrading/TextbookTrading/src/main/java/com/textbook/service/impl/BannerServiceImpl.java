package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.entity.Banner;
import com.textbook.mapper.BannerMapper;
import com.textbook.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图服务实现类
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> listEnabled() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, Constants.STATUS_NORMAL)
               .orderByAsc(Banner::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<Banner> listAll() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Banner::getSort);
        return this.list(wrapper);
    }

    @Override
    public void addBanner(Banner banner) {
        if (banner.getStatus() == null) {
            banner.setStatus(Constants.STATUS_NORMAL);
        }
        this.save(banner);
    }

    @Override
    public void updateBanner(Banner banner) {
        this.updateById(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        this.removeById(id);
    }
}
