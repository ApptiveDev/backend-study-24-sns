package com.example.sns.dto;

import com.example.sns.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long authorId,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getAuthor().getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }
}
