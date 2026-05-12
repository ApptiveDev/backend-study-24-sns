package com.example.sns.dto;

import java.time.LocalDateTime;

// 유저 정보 응답 //비밀번호 절대 포함 금지
public record UserResponseDto(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
) {}