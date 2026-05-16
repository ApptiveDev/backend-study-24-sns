package com.example.sns.dto;

import com.example.sns.entity.Comment;

public record CommentResponse(
        Long id,
        String content,
        String authorName
) {
    // Entity를 DTO로 예쁘게 포장하는 마법의 메서드
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName()
        );
    }
}