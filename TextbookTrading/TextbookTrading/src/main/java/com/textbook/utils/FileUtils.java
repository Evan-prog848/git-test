package com.textbook.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * 上传文件
     *
     * @param file       文件
     * @param uploadPath 上传路径
     * @return 文件访问路径
     */
    public static String upload(MultipartFile file, String uploadPath) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成新文件名
        String newFilename = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 创建目录
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        File dest = new File(dir, newFilename);
        file.transferTo(dest);

        return "/uploads/" + newFilename;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public static void delete(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件后缀
     */
    public static String getSuffix(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

    /**
     * 检查文件类型是否为图片
     */
    public static boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
