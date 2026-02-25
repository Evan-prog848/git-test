package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textbook.dto.CreditReportDTO;
import com.textbook.entity.CreditRecord;
import com.textbook.entity.Order;
import com.textbook.entity.User;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.CreditRecordMapper;
import com.textbook.mapper.OrderMapper;
import com.textbook.mapper.UserMapper;
import com.textbook.service.CreditService;
import com.textbook.utils.UserContext;
import com.textbook.vo.CreditRecordVO;
import org.springframework.beans.BeanUtils;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 信用服务实现类
 */
@Service
public class CreditServiceImpl extends ServiceImpl<CreditRecordMapper, CreditRecord> implements CreditService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int POSITIVE_REVIEW_SCORE = 2;
    private static final int NEGATIVE_REVIEW_SCORE = -5;
    private static final int NO_SHOW_SCORE = -10;
    private static final int MISMATCH_SCORE = -15;

    @Override
    public Integer getUserCreditScore(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user.getCreditScore() != null ? user.getCreditScore() : 100;
    }

    @Override
    @Transactional
    public void addReviewCredit(Long userId, Long orderId, boolean isPositive) {
        CreditRecord record = new CreditRecord();
        record.setUserId(userId);
        record.setOrderId(orderId);
        record.setType(isPositive ? 3 : 4);
        record.setScoreChange(isPositive ? POSITIVE_REVIEW_SCORE : NEGATIVE_REVIEW_SCORE);
        record.setDescription(isPositive ? "交易好评奖励" : "交易差评扣分");
        record.setStatus(1);
        save(record);

        updateUserCreditScore(userId, record.getScoreChange());
    }

    @Override
    @Transactional
    public void reportUser(CreditReportDTO dto) {
        Long reporterId = UserContext.getUserId();
        if (reporterId.equals(dto.getUserId())) {
            throw new BusinessException("不能举报自己");
        }

        CreditRecord record = new CreditRecord();
        record.setUserId(dto.getUserId());
        record.setOrderId(dto.getOrderId());
        record.setType(dto.getType());
        record.setScoreChange(0);
        record.setDescription(dto.getDescription());
        record.setReporterId(reporterId);
        record.setStatus(0);

        if (dto.getEvidenceImages() != null && !dto.getEvidenceImages().isEmpty()) {
            try {
                record.setEvidence(objectMapper.writeValueAsString(dto.getEvidenceImages()));
            } catch (JsonProcessingException e) {
                throw new BusinessException("证据图片处理失败");
            }
        }

        save(record);
    }

    @Override
    @Transactional
    public void handleReport(Long recordId, boolean approved, String remark) {
        CreditRecord record = getById(recordId);
        if (record == null) {
            throw new BusinessException("举报记录不存在");
        }
        if (record.getStatus() == 1) {
            throw new BusinessException("该举报已处理");
        }

        if (approved) {
            int scoreChange = record.getType() == 1 ? NO_SHOW_SCORE : MISMATCH_SCORE;
            record.setScoreChange(scoreChange);
            record.setType(5);
            updateUserCreditScore(record.getUserId(), scoreChange);
        }

        record.setStatus(1);
        if (remark != null) {
            record.setDescription(record.getDescription() + " | 处理备注：" + remark);
        }
        updateById(record);
    }

    @Override
    public Page<CreditRecordVO> getUserCreditRecords(Long userId, Integer pageNum, Integer pageSize) {
        Page<CreditRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CreditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CreditRecord::getUserId, userId)
                .eq(CreditRecord::getStatus, 1)
                .orderByDesc(CreditRecord::getCreateTime);
        page = page(page, wrapper);

        Page<CreditRecordVO> voPage = new Page<>(pageNum, pageSize, page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Page<CreditRecordVO> getPendingReports(Integer pageNum, Integer pageSize) {
        Page<CreditRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CreditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CreditRecord::getStatus, 0)
                .orderByAsc(CreditRecord::getCreateTime);
        page = page(page, wrapper);

        Page<CreditRecordVO> voPage = new Page<>(pageNum, pageSize, page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Page<CreditRecordVO> getProcessedReports(Integer pageNum, Integer pageSize) {
        Page<CreditRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CreditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CreditRecord::getStatus, 1, 2)
                .orderByDesc(CreditRecord::getCreateTime);
        page = page(page, wrapper);

        Page<CreditRecordVO> voPage = new Page<>(pageNum, pageSize, page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    private void updateUserCreditScore(Long userId, int scoreChange) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            int currentScore = user.getCreditScore() != null ? user.getCreditScore() : 100;
            int newScore = Math.max(0, currentScore + scoreChange);
            user.setCreditScore(newScore);
            userMapper.updateById(user);
        }
    }

    private CreditRecordVO convertToVO(CreditRecord record) {
        CreditRecordVO vo = new CreditRecordVO();
        BeanUtils.copyProperties(record, vo);

        User user = userMapper.selectById(record.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setNickname(user.getNickname());
            vo.setReportedName(user.getNickname());
        }

        if (record.getOrderId() != null) {
            Order order = orderMapper.selectById(record.getOrderId());
            if (order != null) {
                vo.setOrderNo(order.getOrderNo());
            }
        }

        if (record.getReporterId() != null) {
            User reporter = userMapper.selectById(record.getReporterId());
            if (reporter != null) {
                vo.setReporterName(reporter.getNickname());
            }
        }

        if (record.getEvidence() != null) {
            try {
                List<String> images = Arrays.asList(objectMapper.readValue(record.getEvidence(), String[].class));
                vo.setEvidenceImages(images);
            } catch (JsonProcessingException e) {
                vo.setEvidenceImages(Collections.emptyList());
            }
        }

        vo.setTypeName(getTypeName(record.getType()));
        return vo;
    }

    private String getTypeName(Integer type) {
        if (type == null) return "";
        switch (type) {
            case 1: return "实物与描述不符";
            case 2: return "爽约";
            case 3: return "态度恶劣";
            case 4: return "其他违规";
            default: return "未知";
        }
    }
}
