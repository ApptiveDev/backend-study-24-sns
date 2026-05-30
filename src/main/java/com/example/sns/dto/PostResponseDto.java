package com.example.sns.dto;

import java.time.LocalDateTime;

// DB에서 조회한 게시글 정보를 사용자에게 보여줄 때 사용.
public record PostResponseDto(
        Long id,
        String title,
        String content,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
