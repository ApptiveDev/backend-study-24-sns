package com.example.sns.dto;

// 로그인 성공 시 Access Token을 반환하는 DTO다.
public record UserLoginResponse(
        String accessToken
) {}