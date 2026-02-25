package com.textbook.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.LoginDTO;
import com.textbook.dto.PasswordDTO;
import com.textbook.dto.RegisterDTO;
import com.textbook.dto.UserUpdateDTO;
import com.textbook.entity.Favorite;
import com.textbook.entity.Order;
import com.textbook.entity.User;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.UserMapper;
import com.textbook.service.FavoriteService;
import com.textbook.service.FollowService;
import com.textbook.service.OrderService;
import com.textbook.service.ReviewService;
import com.textbook.service.TextbookService;
import com.textbook.service.UserService;
import com.textbook.utils.JwtUtils;
import com.textbook.utils.UserContext;
import com.textbook.vo.LoginVO;
import com.textbook.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    @Lazy
    private TextbookService textbookService;

    @Autowired
    @Lazy
    private FollowService followService;

    @Autowired
    @Lazy
    private ReviewService reviewService;

    @Autowired
    @Lazy
    private OrderService orderService;

    @Autowired
    @Lazy
    private FavoriteService favoriteService;

    @Override
    public void register(RegisterDTO dto) {
        // 校验两次密码是否一致
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(DigestUtil.md5Hex(dto.getPassword()));
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setIsVerified(Constants.VERIFIED_NO);
        user.setStatus(Constants.STATUS_NORMAL);

        this.save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = this.getOne(wrapper);

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 校验密码
        if (!user.getPassword().equals(DigestUtil.md5Hex(dto.getPassword()))) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == Constants.STATUS_DISABLE) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), Constants.USER_TYPE_USER);

        // 构建返回对象
        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setIsVerified(user.getIsVerified());
        vo.setToken(token);
        vo.setUserType(Constants.USER_TYPE_USER);

        return vo;
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = UserContext.getUserId();
        return getUserProfile(userId);
    }

    @Override
    public void updateUserInfo(UserUpdateDTO dto) {
        Long userId = UserContext.getUserId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新信息
        if (StringUtils.hasText(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (StringUtils.hasText(dto.getAvatar())) {
            user.setAvatar(dto.getAvatar());
        }
        if (StringUtils.hasText(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (StringUtils.hasText(dto.getSchool())) {
            user.setSchool(dto.getSchool());
        }
        if (StringUtils.hasText(dto.getCollege())) {
            user.setCollege(dto.getCollege());
        }
        if (StringUtils.hasText(dto.getMajor())) {
            user.setMajor(dto.getMajor());
        }

        // 检查是否可以成为认证用户
        if (StringUtils.hasText(user.getSchool()) &&
            StringUtils.hasText(user.getCollege()) &&
            StringUtils.hasText(user.getMajor())) {
            user.setIsVerified(Constants.VERIFIED_YES);
        }

        this.updateById(user);
    }

    @Override
    public void updatePassword(PasswordDTO dto) {
        // 校验两次密码是否一致
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }

        Long userId = UserContext.getUserId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 校验原密码
        if (!user.getPassword().equals(DigestUtil.md5Hex(dto.getOldPassword()))) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        user.setPassword(DigestUtil.md5Hex(dto.getNewPassword()));
        this.updateById(user);
    }

    @Override
    public UserVO getUserProfile(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        // 获取统计信息
        vo.setTextbookCount((int) textbookService.count(
            new LambdaQueryWrapper<com.textbook.entity.Textbook>()
                .eq(com.textbook.entity.Textbook::getUserId, userId)
                .eq(com.textbook.entity.Textbook::getStatus, Constants.TEXTBOOK_STATUS_ON)
        ));
        vo.setFollowCount(followService.getFollowingCount(userId));
        vo.setFansCount(followService.getFansCount(userId));
        vo.setOrderCount(orderService.count(
            new LambdaQueryWrapper<Order>()
                .and(wrapper -> wrapper.eq(Order::getBuyerId, userId)
                                       .or()
                                       .eq(Order::getSellerId, userId))
        ));
        vo.setFavoriteCount(favoriteService.count(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
        ));
        vo.setGoodRate(reviewService.getGoodRate(userId));

        return vo;
    }

    @Override
    public Page<User> pageList(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword)
                   .or()
                   .like(User::getNickname, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void updateStatus(Long userId, Integer status) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        this.updateById(user);
    }

    @Override
    public java.util.Map<String, Object> getUserDetail(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("phone", user.getPhone());
        result.put("email", user.getEmail());
        result.put("school", user.getSchool());
        result.put("college", user.getCollege());
        result.put("major", user.getMajor());
        // result.put("gender", user.getGender()); // User实体没有gender字段
        result.put("isVerified", user.getIsVerified());
        result.put("status", user.getStatus());
        result.put("createTime", user.getCreateTime());

        // 统计数据
        result.put("textbookCount", textbookService.count(
            new LambdaQueryWrapper<com.textbook.entity.Textbook>()
                .eq(com.textbook.entity.Textbook::getUserId, userId)
        ));
        result.put("orderCount", orderService.count(
            new LambdaQueryWrapper<Order>()
                .and(wrapper -> wrapper.eq(Order::getBuyerId, userId)
                                       .or()
                                       .eq(Order::getSellerId, userId))
        ));
        result.put("favoriteCount", favoriteService.count(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
        ));
        result.put("followCount", followService.getFollowingCount(userId));
        result.put("fansCount", followService.getFansCount(userId));
        result.put("goodRate", reviewService.getGoodRate(userId));

        return result;
    }

    @Override
    public void adminAddUser(User user) {
        // 检查用户名是否已存在
        User existUser = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername()));
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        // 设置默认密码并加密
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("123456");
        }
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(Constants.STATUS_NORMAL);
        }
        this.save(user);
    }

    @Override
    public void resetPassword(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(DigestUtil.md5Hex("123456"));
        this.updateById(user);
    }
}
