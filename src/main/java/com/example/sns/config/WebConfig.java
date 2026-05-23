package com.example.sns.config;

import com.example.sns.auth.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)  // 어떤 Interceptor를
                .addPathPatterns("/api/**")      // 모든 api 경로에 적용
                .excludePathPatterns(            // 아래 경로는 제외
                        "/api/users",            // 회원가입
                        "/api/users/login"       // 로그인
                );
    }
}
