package com.example.sns.dto;

import com.example.sns.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long userId,
        String content,
        int likeCount,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getContent(),
                post.getLikeCount(),
                post.getCreatedAt()
        );
    }
}
