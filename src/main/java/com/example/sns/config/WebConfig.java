package com.example.sns.config;

import com.example.sns.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 일단 모든 API 경로에 문지기를 세움
                .excludePathPatterns(
                        "/users/signup", // 회원가입은 토큰 없이 접근 가능해야 함
                        "/users/login"   // 로그인도 토큰 없이 접근 가능해야 함
                        // 추가로 열어둘 경로가 있다면 여기에 작성
                );
    }
}