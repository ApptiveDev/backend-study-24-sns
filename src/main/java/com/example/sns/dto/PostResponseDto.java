package com.example.sns.dto;

import com.example.sns.entity.Post;

public record PostResponseDto (
    Long id,
    String title,
    String content
){
    public PostResponseDto(Post post){
        this(post.getId(), post.getTitle(), post.getContent());
    }
}
