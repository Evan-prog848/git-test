package com.textbook.config;

import com.textbook.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        // 用户登录注册
                        "/api/user/login",
                        "/api/user/register",
                        // 管理员登录
                        "/api/admin/login",
                        // 公开访问的接口
                        "/api/textbook/list",
                        "/api/textbook/detail/**",
                        "/api/category/list",
                        "/api/wanted/list",
                        "/api/banner/list",
                        "/api/notice/list",
                        // 校区、课程、交易点公开接口
                        "/api/campus/list",
                        "/api/course/**",
                        "/api/trading-point/list",
                        "/api/isbn/query",
                        "/api/isbn/proxy-image",
                        // 文件上传访问
                        "/uploads/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
