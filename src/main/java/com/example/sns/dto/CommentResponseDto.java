package com.example.sns.dto;

import java.time.LocalDateTime;

// 댓글 목록등등 댓글 관련 응답
public record CommentResponseDto(
        Long id,
        String content,
        String authorName,
        LocalDateTime createdAt
) {}