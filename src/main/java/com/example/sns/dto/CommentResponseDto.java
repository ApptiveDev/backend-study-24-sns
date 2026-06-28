package com.example.sns.dto;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long commentId,
        String content,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
