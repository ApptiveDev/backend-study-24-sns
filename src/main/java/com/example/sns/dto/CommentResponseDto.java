package com.example.sns.dto;

import com.example.sns.entity.Comment;


public record CommentResponseDto(
    Long id,
    String content
){
    public CommentResponseDto(Comment comment){
        this(comment.getId(), comment.getContent());
    }
}
