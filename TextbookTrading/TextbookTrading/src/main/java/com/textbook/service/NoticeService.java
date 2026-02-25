package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.Notice;

/**
 * 公告服务接口
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 分页查询公告列表
     */
    Page<Notice> pageList(Integer pageNum, Integer pageSize);

    /**
     * 添加公告
     */
    void addNotice(Notice notice);

    /**
     * 更新公告
     */
    void updateNotice(Notice notice);

    /**
     * 删除公告
     */
    void deleteNotice(Long id);
}
