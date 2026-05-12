package com.example.sns.dto;

// 회원가입 요청
public record UserSignupRequestDto(
        String username,
        String password,
        String email
) {}