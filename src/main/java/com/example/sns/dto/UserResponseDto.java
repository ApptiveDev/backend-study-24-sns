package com.example.sns.dto;

// 유저 정보를 안전하게 보여주는데 사용
public record UserResponseDto(
        Long id,
        String username
        // password 담으면 안됨!!
) {}
