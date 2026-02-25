package com.textbook.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.dto.CreditReportDTO;
import com.textbook.entity.CreditRecord;
import com.textbook.vo.CreditRecordVO;

/**
 * 信用服务接口
 */
public interface CreditService extends IService<CreditRecord> {

    /**
     * 获取用户信用分
     */
    Integer getUserCreditScore(Long userId);

    /**
     * 添加信用记录（好评/差评）
     */
    void addReviewCredit(Long userId, Long orderId, boolean isPositive);

    /**
     * 举报用户（爽约/实物不符）
     */
    void reportUser(CreditReportDTO dto);

    /**
     * 处理举报
     */
    void handleReport(Long recordId, boolean approved, String remark);

    /**
     * 获取用户信用记录列表
     */
    Page<CreditRecordVO> getUserCreditRecords(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取待处理举报列表（管理员）
     */
    Page<CreditRecordVO> getPendingReports(Integer pageNum, Integer pageSize);

    /**
     * 获取已处理举报列表（管理员）
     */
    Page<CreditRecordVO> getProcessedReports(Integer pageNum, Integer pageSize);
}
