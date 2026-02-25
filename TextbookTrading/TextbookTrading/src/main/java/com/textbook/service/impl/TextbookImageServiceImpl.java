package com.textbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.TextbookImage;
import com.textbook.mapper.TextbookImageMapper;
import com.textbook.service.TextbookImageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教材图片服务实现类
 */
@Service
public class TextbookImageServiceImpl extends ServiceImpl<TextbookImageMapper, TextbookImage> implements TextbookImageService {

    @Override
    public List<String> getImagesByTextbookId(Long textbookId) {
        LambdaQueryWrapper<TextbookImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TextbookImage::getTextbookId, textbookId)
               .orderByAsc(TextbookImage::getSort);
        List<TextbookImage> list = this.list(wrapper);
        return list.stream().map(TextbookImage::getImageUrl).collect(Collectors.toList());
    }

    @Override
    public void saveImages(Long textbookId, List<String> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        List<TextbookImage> list = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            TextbookImage image = new TextbookImage();
            image.setTextbookId(textbookId);
            image.setImageUrl(images.get(i));
            image.setSort(i);
            list.add(image);
        }
        this.saveBatch(list);
    }

    @Override
    public void deleteByTextbookId(Long textbookId) {
        LambdaQueryWrapper<TextbookImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TextbookImage::getTextbookId, textbookId);
        this.remove(wrapper);
    }
}
