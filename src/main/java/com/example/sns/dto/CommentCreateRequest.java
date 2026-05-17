package com.example.sns.dto;

public record CommentCreateRequest(
        Long userId,
        Long postId,
        String content
) {
}