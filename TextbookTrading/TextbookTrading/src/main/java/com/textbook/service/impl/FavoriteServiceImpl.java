package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Favorite;
import com.textbook.entity.Textbook;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.FavoriteMapper;
import com.textbook.service.FavoriteService;
import com.textbook.service.TextbookService;
import com.textbook.utils.UserContext;
import com.textbook.vo.TextbookVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Autowired
    @Lazy
    private TextbookService textbookService;

    @Override
    public void addFavorite(Long textbookId) {
        Long userId = UserContext.getUserId();

        // 检查教材是否存在
        Textbook textbook = textbookService.getById(textbookId);
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }

        // 检查是否已收藏
        if (isFavorite(textbookId)) {
            throw new BusinessException("已收藏此教材");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setTextbookId(textbookId);
        this.save(favorite);
    }

    @Override
    public void removeFavorite(Long textbookId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getTextbookId, textbookId);
        this.remove(wrapper);
    }

    @Override
    public Boolean toggleFavorite(Long textbookId) {
        if (isFavorite(textbookId)) {
            removeFavorite(textbookId);
            return false;
        } else {
            addFavorite(textbookId);
            return true;
        }
    }

    @Override
    public Boolean isFavorite(Long textbookId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return false;
        }

        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getTextbookId, textbookId);
        return this.count(wrapper) > 0;
    }

    @Override
    public Page<TextbookVO> myFavorites(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();

        Page<Favorite> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .orderByDesc(Favorite::getCreateTime);

        Page<Favorite> result = this.page(page, wrapper);

        // 转换为教材VO
        Page<TextbookVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<TextbookVO> voList = result.getRecords().stream()
                .map(f -> {
                    Textbook textbook = textbookService.getById(f.getTextbookId());
                    if (textbook != null) {
                        TextbookVO vo = new TextbookVO();
                        BeanUtils.copyProperties(textbook, vo);
                        vo.setIsFavorite(true);
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
    public Integer getFavoriteCount(Long textbookId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getTextbookId, textbookId);
        return Math.toIntExact(this.count(wrapper));
    }
}
