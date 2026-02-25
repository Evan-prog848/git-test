package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.WantedDTO;
import com.textbook.service.WantedService;
import com.textbook.vo.WantedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 求购控制器
 */
@Api(tags = "求购模块")
@RestController
@RequestMapping("/api/wanted")
public class WantedController {

    @Autowired
    private WantedService wantedService;

    @ApiOperation("发布求购")
    @PostMapping("/publish")
    public Result<Long> publish(@RequestBody WantedDTO dto) {
        Long id = wantedService.publish(dto);
        return Result.success(id);
    }

    @ApiOperation("更新求购")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody WantedDTO dto) {
        wantedService.update(dto);
        return Result.success();
    }

    @ApiOperation("求购列表")
    @GetMapping("/list")
    public Result<Page<WantedVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<WantedVO> page = wantedService.pageList(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @ApiOperation("我的求购列表")
    @GetMapping("/my")
    public Result<Page<WantedVO>> myWanted(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<WantedVO> page = wantedService.myWanted(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("关闭求购")
    @PutMapping("/close/{id}")
    public Result<Void> close(@PathVariable Long id) {
        wantedService.closeWanted(id);
        return Result.success();
    }

    @ApiOperation("删除求购")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        wantedService.deleteWanted(id);
        return Result.success();
    }
}
