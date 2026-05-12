package com.example.sns.dto;

import java.time.LocalDateTime;

// 게시판 목록이나 상세보기 등등 Post관련 응답
public record PostResponseDto(
        Long id,
        String title,
        String content,
        String authorName, //이름만 주기
        int viewCount,
        LocalDateTime createdAt
) {}