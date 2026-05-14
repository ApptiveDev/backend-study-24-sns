package com.example.sns.comment.dto;


public record CommentCreateRequest(
        Long userId,
        String content) {
}
