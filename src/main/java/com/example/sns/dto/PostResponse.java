package com.example.sns.dto;

import com.example.sns.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long postId,
        String title,
        String content,
        Long writerId,
        String writerNickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
