package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.TextbookDTO;
import com.textbook.dto.TextbookQueryDTO;
import com.textbook.entity.Textbook;
import com.textbook.vo.TextbookVO;

/**
 * 教材服务接口
 */
public interface TextbookService extends IService<Textbook> {

    /**
     * 发布教材
     */
    Long publish(TextbookDTO dto);

    /**
     * 更新教材
     */
    void update(TextbookDTO dto);

    /**
     * 分页查询教材列表
     */
    Page<TextbookVO> pageList(TextbookQueryDTO query);

    /**
     * 获取教材详情
     */
    TextbookVO getDetail(Long id);

    /**
     * 我的发布列表
     */
    Page<TextbookVO> myPublish(Integer pageNum, Integer pageSize, Integer status);

    /**
     * 上架/下架教材
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除教材
     */
    void deleteTextbook(Long id);

    /**
     * 增加浏览量
     */
    void increaseViewCount(Long id);

    /**
     * 审核教材（后台）
     */
    void audit(Long id, Integer auditStatus, String auditRemark);

    /**
     * 分页查询教材列表（后台）
     */
    Page<TextbookVO> adminPageList(Integer pageNum, Integer pageSize, String title, Long categoryId, Integer status, Integer auditStatus);
}
