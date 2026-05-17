package com.example.sns.dto;

public record PostCreateRequest(
        Long userId,
        String title,
        String content
) {
}