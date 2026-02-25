package com.textbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.textbook.entity.TextbookImage;

import java.util.List;

/**
 * 教材图片服务接口
 */
public interface TextbookImageService extends IService<TextbookImage> {

    /**
     * 根据教材ID获取图片列表
     */
    List<String> getImagesByTextbookId(Long textbookId);

    /**
     * 保存教材图片
     */
    void saveImages(Long textbookId, List<String> images);

    /**
     * 删除教材图片
     */
    void deleteByTextbookId(Long textbookId);
}
