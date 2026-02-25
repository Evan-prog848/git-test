package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Favorite;
import com.textbook.vo.TextbookVO;

/**
 * 收藏服务接口
 */
public interface FavoriteService extends IService<Favorite> {

    /**
     * 添加收藏
     */
    void addFavorite(Long textbookId);

    /**
     * 取消收藏
     */
    void removeFavorite(Long textbookId);

    /**
     * 切换收藏状态
     */
    Boolean toggleFavorite(Long textbookId);

    /**
     * 是否已收藏
     */
    Boolean isFavorite(Long textbookId);

    /**
     * 我的收藏列表
     */
    Page<TextbookVO> myFavorites(Integer pageNum, Integer pageSize);

    /**
     * 获取教材收藏数量
     */
    Integer getFavoriteCount(Long textbookId);
}
