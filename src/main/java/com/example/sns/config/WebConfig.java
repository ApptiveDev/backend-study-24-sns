package com.example.sns.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.sns.auth.JwtAuthInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtAuthInterceptor)
            .addPathPatterns(
    "/posts/**",
                 "/users/**",
                 "/home" )
            .excludePathPatterns(
    "/auth/**",
                "/login",
                "/logout",                    
                "/error",
                "/favicon.ico",
                "/css/**",
                "/js/**",
                "/images/**"

            );
    }
}
