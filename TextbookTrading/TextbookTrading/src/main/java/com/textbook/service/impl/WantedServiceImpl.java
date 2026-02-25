package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.WantedDTO;
import com.textbook.entity.User;
import com.textbook.entity.Wanted;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.WantedMapper;
import com.textbook.service.UserService;
import com.textbook.service.WantedService;
import com.textbook.utils.UserContext;
import com.textbook.vo.WantedVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 求购服务实现类
 */
@Service
public class WantedServiceImpl extends ServiceImpl<WantedMapper, Wanted> implements WantedService {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public Long publish(WantedDTO dto) {
        Long userId = UserContext.getUserId();

        Wanted wanted = new Wanted();
        BeanUtils.copyProperties(dto, wanted);
        wanted.setUserId(userId);
        wanted.setStatus(Constants.STATUS_NORMAL);

        this.save(wanted);
        return wanted.getId();
    }

    @Override
    public void update(WantedDTO dto) {
        Long userId = UserContext.getUserId();

        Wanted wanted = this.getById(dto.getId());
        if (wanted == null) {
            throw new BusinessException("求购信息不存在");
        }
        if (!wanted.getUserId().equals(userId)) {
            throw new BusinessException("无权修改此求购信息");
        }

        BeanUtils.copyProperties(dto, wanted);
        this.updateById(wanted);
    }

    @Override
    public Page<WantedVO> pageList(Integer pageNum, Integer pageSize, String keyword) {
        Page<Wanted> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Wanted> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Wanted::getStatus, Constants.STATUS_NORMAL);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Wanted::getTitle, keyword)
                             .or()
                             .like(Wanted::getAuthor, keyword));
        }
        wrapper.orderByDesc(Wanted::getCreateTime);

        Page<Wanted> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    @Override
    public Page<WantedVO> myWanted(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();

        Page<Wanted> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Wanted> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Wanted::getUserId, userId)
               .orderByDesc(Wanted::getCreateTime);

        Page<Wanted> result = this.page(page, wrapper);
        return convertToVOPage(result);
    }

    @Override
    public void closeWanted(Long id) {
        Long userId = UserContext.getUserId();

        Wanted wanted = this.getById(id);
        if (wanted == null) {
            throw new BusinessException("求购信息不存在");
        }
        if (!wanted.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此求购信息");
        }

        wanted.setStatus(Constants.STATUS_DISABLE);
        this.updateById(wanted);
    }

    @Override
    public void deleteWanted(Long id) {
        Long userId = UserContext.getUserId();

        Wanted wanted = this.getById(id);
        if (wanted == null) {
            throw new BusinessException("求购信息不存在");
        }
        if (!wanted.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此求购信息");
        }

        this.removeById(id);
    }

    private Page<WantedVO> convertToVOPage(Page<Wanted> page) {
        Page<WantedVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WantedVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    private WantedVO convertToVO(Wanted wanted) {
        WantedVO vo = new WantedVO();
        BeanUtils.copyProperties(wanted, vo);

        User user = userService.getById(wanted.getUserId());
        if (user != null) {
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }

        return vo;
    }
}
