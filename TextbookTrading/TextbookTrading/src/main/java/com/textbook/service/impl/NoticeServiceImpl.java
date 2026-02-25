package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.entity.Notice;
import com.textbook.mapper.NoticeMapper;
import com.textbook.service.NoticeService;
import org.springframework.stereotype.Service;

/**
 * 公告服务实现类
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Page<Notice> pageList(Integer pageNum, Integer pageSize) {
        Page<Notice> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, Constants.STATUS_NORMAL)
               .orderByDesc(Notice::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    public void addNotice(Notice notice) {
        notice.setStatus(Constants.STATUS_NORMAL);
        this.save(notice);
    }

    @Override
    public void updateNotice(Notice notice) {
        this.updateById(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        this.removeById(id);
    }
}
