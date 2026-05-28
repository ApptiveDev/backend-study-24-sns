package com.example.sns.dto;

import com.example.sns.entity.Post;

// 게시글 조회 시 클라이언트에게 반환하는 DTO다.
public record PostResponse(
        Long id,
        String title,
        String content,
        Long userId,
        String userName
) {
    // Post 엔티티를 PostResponse DTO로 변환하는 편의 메서드다.
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getName()
        );
    }
}