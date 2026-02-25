package com.textbook.controller;

import com.textbook.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Api(tags = "文件上传模块")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload.path:../TextbookTradingWeb/public/images/books/covers/}")
    private String uploadPath;

    @Value("${file.access.url:/images/books}")
    private String accessUrl;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file, 
                                  @RequestParam(value = "type", defaultValue = "cover") String type) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        // 生成新文件名
        String newFilename = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 根据类型确定上传目录
        // 使用绝对路径，指向项目的前端public目录
        String projectRoot = System.getProperty("user.dir");
        String baseDir = projectRoot + "/../TextbookTradingWeb/public/images/books/";
        String subDir;
        switch (type) {
            case "avatar":
                subDir = "avatars/";
                break;
            case "banner":
                subDir = "banners/";
                break;
            case "detail":
                subDir = "details/";
                break;
            case "cover":
            default:
                subDir = "covers/";
                break;
        }
        
        String fullPath = baseDir + subDir;

        // 创建目录
        File uploadDir = new File(fullPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        File destFile = new File(fullPath + newFilename);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败: " + e.getMessage());
        }

        // 返回访问URL（前端可直接访问的路径）
        String url = "/images/books/" + subDir + newFilename;
        return Result.success(url);
    }

    @ApiOperation("上传多个文件")
    @PostMapping("/uploads")
    public Result<String[]> uploads(@RequestParam("files") MultipartFile[] files,
                                     @RequestParam(value = "type", defaultValue = "detail") String type) {
        if (files == null || files.length == 0) {
            return Result.error("请选择要上传的文件");
        }

        // 根据类型确定上传目录
        // 使用绝对路径，指向项目的前端public目录
        String projectRoot = System.getProperty("user.dir");
        String baseDir = projectRoot + "/../TextbookTradingWeb/public/images/books/";
        String subDir;
        switch (type) {
            case "avatar":
                subDir = "avatars/";
                break;
            case "banner":
                subDir = "banners/";
                break;
            case "detail":
                subDir = "details/";
                break;
            case "cover":
            default:
                subDir = "covers/";
                break;
        }
        
        String fullPath = baseDir + subDir;

        String[] urls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file.isEmpty()) {
                continue;
            }

            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFilename = UUID.randomUUID().toString().replace("-", "") + suffix;

            File uploadDir = new File(fullPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(fullPath + newFilename);
            try {
                file.transferTo(destFile);
                urls[i] = "/images/books/" + subDir + newFilename;
            } catch (IOException e) {
                urls[i] = null;
            }
        }

        return Result.success(urls);
    }
}
