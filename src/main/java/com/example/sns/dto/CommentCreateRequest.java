package com.example.sns.dto;

public record CommentCreateRequest(
        Long postId,
        String content
) {
}