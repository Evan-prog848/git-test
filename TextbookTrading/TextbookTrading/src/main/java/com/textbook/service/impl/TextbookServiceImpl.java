package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.common.Constants;
import com.textbook.dto.TextbookDTO;
import com.textbook.dto.TextbookQueryDTO;
import com.textbook.entity.Category;
import com.textbook.entity.Textbook;
import com.textbook.entity.User;
import com.textbook.exception.BusinessException;
import com.textbook.mapper.TextbookMapper;
import com.textbook.service.*;
import com.textbook.utils.UserContext;
import com.textbook.vo.TextbookVO;
import com.textbook.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 教材服务实现类
 */
@Service
public class TextbookServiceImpl extends ServiceImpl<TextbookMapper, Textbook> implements TextbookService {

    @Autowired
    private TextbookImageService textbookImageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private FavoriteService favoriteService;

    @Override
    @Transactional
    public Long publish(TextbookDTO dto) {
        Long userId = UserContext.getUserId();

        Textbook textbook = new Textbook();
        BeanUtils.copyProperties(dto, textbook);
        textbook.setUserId(userId);
        textbook.setViewCount(0);
        textbook.setStatus(Constants.TEXTBOOK_STATUS_ON);
        textbook.setAuditStatus(Constants.AUDIT_STATUS_PENDING); // 待审核，需管理员审核通过后才能展示

        this.save(textbook);

        // 保存图片
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            textbookImageService.saveImages(textbook.getId(), dto.getImages());
        }

        return textbook.getId();
    }

    @Override
    @Transactional
    public void update(TextbookDTO dto) {
        Long userId = UserContext.getUserId();

        Textbook textbook = this.getById(dto.getId());
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }
        if (!textbook.getUserId().equals(userId)) {
            throw new BusinessException("无权修改此教材");
        }

        BeanUtils.copyProperties(dto, textbook);
        this.updateById(textbook);

        // 更新图片
        if (dto.getImages() != null) {
            textbookImageService.deleteByTextbookId(textbook.getId());
            if (!dto.getImages().isEmpty()) {
                textbookImageService.saveImages(textbook.getId(), dto.getImages());
            }
        }
    }

    @Override
    public Page<TextbookVO> pageList(TextbookQueryDTO query) {
        Page<Textbook> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 使用自定义SQL，关联user表过滤被封禁用户的教材
        IPage<Textbook> result = baseMapper.selectTextbookPageWithUserStatus(
                page,
                query.getKeyword(),
                query.getCategoryId(),
                query.getCampusId(),
                query.getCourseId(),
                query.getCourseName(),
                query.getMinPrice() != null ? query.getMinPrice().doubleValue() : null,
                query.getMaxPrice() != null ? query.getMaxPrice().doubleValue() : null,
                query.getMinCondition(),
                query.getOrderBy()
        );

        // 转换为VO
        Page<TextbookVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<TextbookVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public TextbookVO getDetail(Long id) {
        Textbook textbook = this.getById(id);
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }

        // 检查发布者状态，如果用户已被封禁，则不允许查看
        // 直接从数据库查询User实体来获取status字段
        User user = userService.getById(textbook.getUserId());
        if (user == null || user.getStatus() == Constants.STATUS_DISABLE) {
            throw new BusinessException("该教材发布者已被封禁，教材不可访问");
        }

        TextbookVO vo = convertToVO(textbook);

        // 获取卖家信息
        UserVO seller = userService.getUserProfile(textbook.getUserId());
        vo.setSeller(seller);

        // 获取图片列表
        List<String> images = textbookImageService.getImagesByTextbookId(id);
        vo.setImages(images);

        // 获取收藏状态和数量
        Long currentUserId = UserContext.getUserId();
        if (currentUserId != null) {
            vo.setIsFavorite(favoriteService.isFavorite(id));
        } else {
            vo.setIsFavorite(false);
        }
        vo.setFavoriteCount(favoriteService.getFavoriteCount(id));

        return vo;
    }

    @Override
    public Page<TextbookVO> myPublish(Integer pageNum, Integer pageSize, Integer status) {
        Long userId = UserContext.getUserId();

        Page<Textbook> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Textbook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Textbook::getUserId, userId);
        if (status != null) {
            wrapper.eq(Textbook::getStatus, status);
        }
        wrapper.orderByDesc(Textbook::getCreateTime);

        Page<Textbook> result = this.page(page, wrapper);

        Page<TextbookVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<TextbookVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Long userId = UserContext.getUserId();

        Textbook textbook = this.getById(id);
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }
        if (!textbook.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此教材");
        }

        textbook.setStatus(status);
        this.updateById(textbook);
    }

    @Override
    @Transactional
    public void deleteTextbook(Long id) {
        Long userId = UserContext.getUserId();

        Textbook textbook = this.getById(id);
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }
        if (!textbook.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此教材");
        }

        // 删除图片
        textbookImageService.deleteByTextbookId(id);
        // 删除教材
        this.removeById(id);
    }

    @Override
    public void increaseViewCount(Long id) {
        Textbook textbook = this.getById(id);
        if (textbook != null) {
            textbook.setViewCount(textbook.getViewCount() + 1);
            this.updateById(textbook);
        }
    }

    @Override
    public void audit(Long id, Integer auditStatus, String auditRemark) {
        Textbook textbook = this.getById(id);
        if (textbook == null) {
            throw new BusinessException("教材不存在");
        }
        textbook.setAuditStatus(auditStatus);
        this.updateById(textbook);

        // 如果审核被拒绝，发送系统消息通知用户
        if (auditStatus == Constants.AUDIT_STATUS_REJECT && auditRemark != null) {
            // TODO: 发送系统消息通知发布者审核结果
        }
    }

    @Override
    public Page<TextbookVO> adminPageList(Integer pageNum, Integer pageSize, String title, Long categoryId, Integer status, Integer auditStatus) {
        Page<Textbook> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Textbook> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(title)) {
            wrapper.like(Textbook::getTitle, title);
        }
        if (categoryId != null) {
            wrapper.eq(Textbook::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Textbook::getStatus, status);
        }
        if (auditStatus != null) {
            wrapper.eq(Textbook::getAuditStatus, auditStatus);
        }
        wrapper.orderByDesc(Textbook::getCreateTime);

        Page<Textbook> result = this.page(page, wrapper);

        // 转换为VO并添加发布者信息
        Page<TextbookVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<TextbookVO> voList = result.getRecords().stream()
                .map(textbook -> {
                    TextbookVO vo = convertToVO(textbook);
                    // 添加发布者信息
                    User seller = userService.getById(textbook.getUserId());
                    if (seller != null) {
                        vo.setSellerName(seller.getNickname());
                        vo.setSellerPhone(seller.getPhone());
                        vo.setSellerAvatar(seller.getAvatar());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    private TextbookVO convertToVO(Textbook textbook) {
        TextbookVO vo = new TextbookVO();
        BeanUtils.copyProperties(textbook, vo);

        // 获取分类名称
        if (textbook.getCategoryId() != null) {
            Category category = categoryService.getById(textbook.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        return vo;
    }
}
