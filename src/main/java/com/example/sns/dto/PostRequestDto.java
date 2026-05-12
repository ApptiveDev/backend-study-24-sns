package com.example.sns.dto;

// 글 작성/수정 요청
public record PostRequestDto(
        String title,
        String content
) {}