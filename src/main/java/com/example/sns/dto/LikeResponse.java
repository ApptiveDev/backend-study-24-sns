package com.example.sns.dto;

import com.example.sns.entity.Like;

import java.time.LocalDateTime;

public record LikeResponse(
        Long likeId,
        Long postId,
        Long userId,
        String userNickname,
        LocalDateTime createdAt
) {
    public static LikeResponse from(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getPost().getId(),
                like.getUser().getId(),
                like.getUser().getNickname(),
                like.getCreatedAt()
        );
    }
}