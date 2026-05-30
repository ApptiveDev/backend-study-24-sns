package com.example.sns.config;

import com.example.sns.auth.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.sns.auth.CookieJwtInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final CookieJwtInterceptor cookieJwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)  // 어떤 Interceptor를
                .addPathPatterns("/api/**")      // 모든 api 경로에 적용
                .excludePathPatterns(            // 아래 경로는 제외
                        "/api/users",            // 회원가입
                        "/api/users/login",       // 로그인
                        "/api/users/refresh"    // 토큰 재발급
                );

        // 뷰 인터셉터 (쿠키 방식) - 로그인 페이지 제외
        registry.addInterceptor(cookieJwtInterceptor)
                .addPathPatterns("/view/**")
                .excludePathPatterns(
                        "/view/login",
                        "/view/register"
                );
    }
}
