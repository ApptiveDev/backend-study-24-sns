package com.example.sns.dto;

// 댓글 요청
public record CommentRequestDto(
        String content,
        Long postId // 어떤 Post의 댓글인지 알아야 함
) {}