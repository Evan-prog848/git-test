package com.textbook.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.entity.Course;
import com.textbook.entity.Major;
import com.textbook.service.CourseService;
import com.textbook.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台 - 专业课程管理控制器
 */
@Api(tags = "管理后台-专业课程管理")
@RestController
@RequestMapping("/api/admin/course")
public class AdminCourseController {

    @Autowired
    private MajorService majorService;

    @Autowired
    private CourseService courseService;

    // ========== 专业管理 ==========

    @ApiOperation("专业列表")
    @GetMapping("/major/list")
    public Result<Page<Major>> majorList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long campusId,
            @RequestParam(required = false) String keyword) {
        Page<Major> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Major> wrapper = new LambdaQueryWrapper<>();
        if (campusId != null) {
            wrapper.eq(Major::getCampusId, campusId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Major::getName, keyword).or().like(Major::getCollege, keyword);
        }
        wrapper.orderByAsc(Major::getId);
        page = majorService.page(page, wrapper);
        return Result.success(page);
    }

    @ApiOperation("所有专业（下拉选择用）")
    @GetMapping("/major/all")
    public Result<List<Major>> majorAll(@RequestParam(required = false) Long campusId) {
        return Result.success(majorService.listByCampusId(campusId));
    }

    @ApiOperation("添加专业")
    @PostMapping("/major/add")
    public Result<Void> addMajor(@RequestBody Major major) {
        majorService.save(major);
        return Result.success();
    }

    @ApiOperation("更新专业")
    @PutMapping("/major/update")
    public Result<Void> updateMajor(@RequestBody Major major) {
        majorService.updateById(major);
        return Result.success();
    }

    @ApiOperation("删除专业")
    @DeleteMapping("/major/{id}")
    public Result<Void> deleteMajor(@PathVariable Long id) {
        majorService.removeById(id);
        return Result.success();
    }

    // ========== 课程管理 ==========

    @ApiOperation("课程列表")
    @GetMapping("/list")
    public Result<Page<Course>> courseList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) String keyword) {
        Page<Course> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (majorId != null) {
            wrapper.eq(Course::getMajorId, majorId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Course::getName, keyword).or().like(Course::getCode, keyword));
        }
        wrapper.orderByAsc(Course::getSemester, Course::getId);
        page = courseService.page(page, wrapper);
        return Result.success(page);
    }

    @ApiOperation("添加课程")
    @PostMapping("/add")
    public Result<Void> addCourse(@RequestBody Course course) {
        courseService.save(course);
        return Result.success();
    }

    @ApiOperation("更新课程")
    @PutMapping("/update")
    public Result<Void> updateCourse(@RequestBody Course course) {
        courseService.updateById(course);
        return Result.success();
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.removeById(id);
        return Result.success();
    }
}
