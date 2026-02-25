package com.textbook.controller;

import com.textbook.common.Result;
import com.textbook.entity.Campus;
import com.textbook.service.CampusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 校区控制器
 */
@Api(tags = "校区模块")
@RestController
@RequestMapping("/api/campus")
public class CampusController {

    @Autowired
    private CampusService campusService;

    @ApiOperation("获取校区列表")
    @GetMapping("/list")
    public Result<List<Campus>> list() {
        List<Campus> list = campusService.listEnabled();
        return Result.success(list);
    }
}
