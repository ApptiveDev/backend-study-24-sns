package com.example.sns.comment.dto;

public record CommentUpdateRequest(
        Long userId,
        String content
) {
}
