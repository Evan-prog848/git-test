package com.textbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.common.Result;
import com.textbook.service.FollowService;
import com.textbook.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 关注控制器
 */
@Api(tags = "关注模块")
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @ApiOperation("关注用户")
    @PostMapping("/add/{userId}")
    public Result<Void> follow(@PathVariable Long userId) {
        followService.follow(userId);
        return Result.success();
    }

    @ApiOperation("取消关注")
    @DeleteMapping("/remove/{userId}")
    public Result<Void> unfollow(@PathVariable Long userId) {
        followService.unfollow(userId);
        return Result.success();
    }

    @ApiOperation("切换关注状态")
    @PostMapping("/toggle/{userId}")
    public Result<Boolean> toggle(@PathVariable Long userId) {
        Boolean isFollowed = followService.toggleFollow(userId);
        return Result.success(isFollowed);
    }

    @ApiOperation("是否已关注")
    @GetMapping("/check/{userId}")
    public Result<Boolean> check(@PathVariable Long userId) {
        Boolean isFollowed = followService.isFollowed(userId);
        return Result.success(isFollowed);
    }

    @ApiOperation("我的关注列表")
    @GetMapping("/following")
    public Result<Page<UserVO>> following(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<UserVO> page = followService.myFollowing(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("我的粉丝列表")
    @GetMapping("/fans")
    public Result<Page<UserVO>> fans(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<UserVO> page = followService.myFans(pageNum, pageSize);
        return Result.success(page);
    }
}
