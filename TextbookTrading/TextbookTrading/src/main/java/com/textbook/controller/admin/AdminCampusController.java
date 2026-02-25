package com.textbook.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.Campus;
import com.textbook.service.CampusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台 - 校区管理控制器
 */
@Api(tags = "管理后台-校区管理")
@RestController
@RequestMapping("/api/admin/campus")
public class AdminCampusController {

    @Autowired
    private CampusService campusService;

    @ApiOperation("校区列表")
    @GetMapping("/list")
    public Result<Page<Campus>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Campus> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Campus> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Campus::getName, keyword);
        }
        wrapper.orderByAsc(Campus::getId);
        page = campusService.page(page, wrapper);
        return Result.success(page);
    }

    @ApiOperation("添加校区")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Campus campus) {
        campusService.save(campus);
        return Result.success();
    }

    @ApiOperation("更新校区")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Campus campus) {
        campusService.updateById(campus);
        return Result.success();
    }

    @ApiOperation("删除校区")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        campusService.removeById(id);
        return Result.success();
    }

    @ApiOperation("更新校区状态")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Campus campus = new Campus();
        campus.setId(id);
        campus.setStatus(status);
        campusService.updateById(campus);
        return Result.success();
    }
}
