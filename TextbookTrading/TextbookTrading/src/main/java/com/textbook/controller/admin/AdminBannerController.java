package com.textbook.controller.admin;

import com.textbook.common.Result;
import com.textbook.entity.Banner;
import com.textbook.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台轮播图管理控制器
 */
@Api(tags = "后台-轮播图管理模块")
@RestController
@RequestMapping("/api/admin/banner")
public class AdminBannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation("获取所有轮播图")
    @GetMapping("/list")
    public Result<List<Banner>> list() {
        List<Banner> list = bannerService.listAll();
        return Result.success(list);
    }

    @ApiOperation("添加轮播图")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Banner banner) {
        bannerService.addBanner(banner);
        return Result.success();
    }

    @ApiOperation("更新轮播图")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Banner banner) {
        bannerService.updateBanner(banner);
        return Result.success();
    }

    @ApiOperation("删除轮播图")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return Result.success();
    }
}
