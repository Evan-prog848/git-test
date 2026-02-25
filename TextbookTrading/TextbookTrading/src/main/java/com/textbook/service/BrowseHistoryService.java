package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.BrowseHistory;
import com.textbook.vo.TextbookVO;

/**
 * 浏览历史服务接口
 */
public interface BrowseHistoryService extends IService<BrowseHistory> {

    /**
     * 添加浏览记录
     */
    void addHistory(Long textbookId);

    /**
     * 我的浏览历史
     */
    Page<TextbookVO> myHistory(Integer pageNum, Integer pageSize);

    /**
     * 清空浏览历史
     */
    void clearHistory();

    /**
     * 删除指定浏览记录
     */
    void deleteHistory(Long id);
}
