package com.example.sns.dto;

public record LikeCreateRequest(
        Long userId,
        Long postId
) {
}