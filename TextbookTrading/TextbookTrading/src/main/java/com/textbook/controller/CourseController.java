package com.textbook.controller;

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
 * 课程控制器
 */
@Api(tags = "课程模块")
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MajorService majorService;

    @ApiOperation("获取专业列表")
    @GetMapping("/major/list")
    public Result<List<Major>> listMajors(@RequestParam(required = false) Long campusId) {
        List<Major> list = majorService.listByCampusId(campusId);
        return Result.success(list);
    }

    @ApiOperation("获取课程列表")
    @GetMapping("/list")
    public Result<List<Course>> listCourses(@RequestParam(required = false) Long majorId) {
        List<Course> list = courseService.listByMajorId(majorId);
        return Result.success(list);
    }

    @ApiOperation("搜索课程")
    @GetMapping("/search")
    public Result<List<Course>> searchCourses(@RequestParam String keyword) {
        List<Course> list = courseService.searchByKeyword(keyword);
        return Result.success(list);
    }
}
