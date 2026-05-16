package com.example.sns.dto;

import com.example.sns.entity.Post;

public record PostResponse(
        Long id,
        String title,
        String content,
        String writerName
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName()
        );
    }
}