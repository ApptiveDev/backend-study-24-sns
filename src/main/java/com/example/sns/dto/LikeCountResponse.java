package com.example.sns.dto;

public record LikeCountResponse(
        Long postId,
        long likeCount
) {
}