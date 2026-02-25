package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.BrowseHistory;
import com.textbook.entity.Textbook;
import com.textbook.mapper.BrowseHistoryMapper;
import com.textbook.service.BrowseHistoryService;
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
 * 浏览历史服务实现类
 */
@Service
public class BrowseHistoryServiceImpl extends ServiceImpl<BrowseHistoryMapper, BrowseHistory> implements BrowseHistoryService {

    @Autowired
    @Lazy
    private TextbookService textbookService;

    @Override
    public void addHistory(Long textbookId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return;
        }

        // 删除旧的浏览记录
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
               .eq(BrowseHistory::getTextbookId, textbookId);
        this.remove(wrapper);

        // 添加新的浏览记录
        BrowseHistory history = new BrowseHistory();
        history.setUserId(userId);
        history.setTextbookId(textbookId);
        this.save(history);
    }

    @Override
    public Page<TextbookVO> myHistory(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();

        Page<BrowseHistory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
               .orderByDesc(BrowseHistory::getCreateTime);

        Page<BrowseHistory> result = this.page(page, wrapper);

        // 转换为教材VO
        Page<TextbookVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<TextbookVO> voList = result.getRecords().stream()
                .map(h -> {
                    Textbook textbook = textbookService.getById(h.getTextbookId());
                    if (textbook != null) {
                        TextbookVO vo = new TextbookVO();
                        BeanUtils.copyProperties(textbook, vo);
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
    public void clearHistory() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId);
        this.remove(wrapper);
    }

    @Override
    public void deleteHistory(Long id) {
        Long userId = UserContext.getUserId();

        BrowseHistory history = this.getById(id);
        if (history != null && history.getUserId().equals(userId)) {
            this.removeById(id);
        }
    }
}
