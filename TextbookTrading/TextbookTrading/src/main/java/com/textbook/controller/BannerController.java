package com.textbook.controller;

import com.textbook.common.Result;
import com.textbook.entity.Banner;
import com.textbook.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图控制器
 */
@Api(tags = "轮播图模块")
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation("获取轮播图列表")
    @GetMapping("/list")
    public Result<List<Banner>> list() {
        List<Banner> list = bannerService.listEnabled();
        return Result.success(list);
    }
}
