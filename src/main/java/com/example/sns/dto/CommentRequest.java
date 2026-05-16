package com.example.sns.dto;

public record CommentRequest(
        Long userId,
        String content
) {
}