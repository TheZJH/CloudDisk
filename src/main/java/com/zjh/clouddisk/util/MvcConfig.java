package com.zjh.clouddisk.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TheZJH
 * @version 1.0
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 注册登录拦截器 addPathPatterns() -> 拦截的请求  excludePathPatterns -> 不拦截的请求
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/login", "page-files", "index", "page-error", "page-error-500", "/assets/**", "/**/*.css",
                        "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif","/**/*.jpeg");
    }
}
