package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Follow;
import com.textbook.entity.User;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.FollowMapper;
import com.textbook.service.FollowService;
import com.textbook.service.MessageService;
import com.textbook.service.UserService;
import com.textbook.utils.UserContext;
import com.textbook.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 关注服务实现类
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private MessageService messageService;

    @Override
    public void follow(Long followUserId) {
        Long userId = UserContext.getUserId();

        if (userId.equals(followUserId)) {
            throw new BusinessException("不能关注自己");
        }

        // 检查用户是否存在
        User followUser = userService.getById(followUserId);
        if (followUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查是否已关注
        if (isFollowed(followUserId)) {
            throw new BusinessException("已关注此用户");
        }

        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowUserId(followUserId);
        this.save(follow);

        // 发送通知
        User currentUser = userService.getById(userId);
        messageService.sendSystemNotice(
            followUserId,
            "新增粉丝",
            currentUser.getNickname() + " 关注了你"
        );
    }

    @Override
    public void unfollow(Long followUserId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
               .eq(Follow::getFollowUserId, followUserId);
        this.remove(wrapper);
    }

    @Override
    public Boolean toggleFollow(Long followUserId) {
        if (isFollowed(followUserId)) {
            unfollow(followUserId);
            return false;
        } else {
            follow(followUserId);
            return true;
        }
    }

    @Override
    public Boolean isFollowed(Long followUserId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return false;
        }

        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
               .eq(Follow::getFollowUserId, followUserId);
        return this.count(wrapper) > 0;
    }

    @Override
    public Page<UserVO> myFollowing(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();

        Page<Follow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
               .orderByDesc(Follow::getCreateTime);

        Page<Follow> result = this.page(page, wrapper);

        // 转换为用户VO
        Page<UserVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<UserVO> voList = result.getRecords().stream()
                .map(f -> {
                    User user = userService.getById(f.getFollowUserId());
                    if (user != null) {
                        UserVO vo = new UserVO();
                        BeanUtils.copyProperties(user, vo);
                        return vo;
                    }
                    return null;
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public Page<UserVO> myFans(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();

        Page<Follow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowUserId, userId)
               .orderByDesc(Follow::getCreateTime);

        Page<Follow> result = this.page(page, wrapper);

        // 转换为用户VO
        Page<UserVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<UserVO> voList = result.getRecords().stream()
                .map(f -> {
                    User user = userService.getById(f.getUserId());
                    if (user != null) {
                        UserVO vo = new UserVO();
                        BeanUtils.copyProperties(user, vo);
                        return vo;
                    }
                    return null;
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public Integer getFollowingCount(Long userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId);
        return Math.toIntExact(this.count(wrapper));
    }

    @Override
    public Integer getFansCount(Long userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowUserId, userId);
        return Math.toIntExact(this.count(wrapper));
    }
}
