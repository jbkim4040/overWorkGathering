package com.overWorkGathering.main.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 가로채는 경로 설정 가능
//        registry.addInterceptor(new LoginInterceptor())
//        .addPathPatterns("/user/auth");

//        registry.addInterceptor(new Interceptor())
//        .addPathPatterns("/*")
//        .excludePathPatterns("/login")
//        .excludePathPatterns("/SignUp")
//        .excludePathPatterns("/FindPw")
//        .excludePathPatterns("/CodeSendPopup")
//        ;


    }
}