package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Follow;
import com.textbook.vo.UserVO;

/**
 * 关注服务接口
 */
public interface FollowService extends IService<Follow> {

    /**
     * 关注用户
     */
    void follow(Long followUserId);

    /**
     * 取消关注
     */
    void unfollow(Long followUserId);

    /**
     * 切换关注状态
     */
    Boolean toggleFollow(Long followUserId);

    /**
     * 是否已关注
     */
    Boolean isFollowed(Long followUserId);

    /**
     * 我的关注列表
     */
    Page<UserVO> myFollowing(Integer pageNum, Integer pageSize);

    /**
     * 我的粉丝列表
     */
    Page<UserVO> myFans(Integer pageNum, Integer pageSize);

    /**
     * 获取关注数
     */
    Integer getFollowingCount(Long userId);

    /**
     * 获取粉丝数
     */
    Integer getFansCount(Long userId);
}
