package com.example.sns.dto;

import com.example.sns.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long userId,
        String username,
        String nickname,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getUser().getNickname(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
