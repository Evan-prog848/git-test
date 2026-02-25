package com.textbook.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.dto.LoginDTO;
import com.textbook.entity.Admin;
import com.textbook.service.AdminService;
import com.textbook.service.StatisticsService;
import com.textbook.vo.LoginVO;
import com.textbook.vo.StatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器
 */
@Api(tags = "后台-管理员模块")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        LoginVO vo = adminService.login(dto);
        return Result.success(vo);
    }

    @ApiOperation("获取当前管理员信息")
    @GetMapping("/info")
    public Result<Admin> getCurrentAdmin() {
        Admin admin = adminService.getCurrentAdmin();
        return Result.success(admin);
    }

    @ApiOperation("获取统计数据")
    @GetMapping("/statistics")
    public Result<StatisticsVO> getStatistics(@RequestParam(defaultValue = "7") Integer days) {
        StatisticsVO vo = statisticsService.getStatistics(days);
        return Result.success(vo);
    }

    @ApiOperation("管理员列表")
    @GetMapping("/list")
    public Result<Page<Admin>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<Admin> page = adminService.pageList(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @ApiOperation("添加管理员")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Admin admin) {
        adminService.addAdmin(admin);
        return Result.success();
    }

    @ApiOperation("更新管理员")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Admin admin) {
        adminService.updateAdmin(admin);
        return Result.success();
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return Result.success();
    }

    @ApiOperation("修改管理员状态")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        adminService.updateStatus(id, status);
        return Result.success();
    }
}
