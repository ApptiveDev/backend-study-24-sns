package com.example.sns.post.dto;

import com.example.sns.comment.dto.CommentResponse;
import com.example.sns.comment.entity.Comment;
import com.example.sns.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

// 댓글 포함 DTO
public record PostDetailResponse (
        Long id,
        String username,
        String title,
        String content,
        LocalDateTime createdAt,
        List<CommentResponse> comments
){
    public static PostDetailResponse from(Post post, List<Comment> comments) {
        return new PostDetailResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                comments.stream()
                        .map(CommentResponse::from)
                        .toList()
        );
    }
}
