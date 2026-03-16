package com.example.demo3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 修改点1：将 allowedOrigins 改为 allowedOriginPatterns，添加通配符支持
        // 修改点2：添加 localhost:5174 到允许的源
        // 修改点3：添加 maxAge 设置预检请求缓存
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 改为通配符，允许所有来源（生产环境应限制）
                // 或者指定具体来源：
                // .allowedOrigins(
                //     "http://localhost:5173",
                //     "http://localhost:5174",
                //     "http://127.0.0.1:5173",
                //     "http://127.0.0.1:5174"
                // )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // 添加 PATCH
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // 预检请求缓存1小时
    }

    // 【新增】配置静态资源映射，让前端可以访问上传的图片
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 因为设置了context-path=/api，所以需要加上/api前缀
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir)
                .setCachePeriod(3600);

        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations("file:" + uploadDir);

        System.out.println("静态资源映射配置: " + uploadDir);
        System.out.println("映射规则: /uploads/** -> file:" + uploadDir);
    }
}