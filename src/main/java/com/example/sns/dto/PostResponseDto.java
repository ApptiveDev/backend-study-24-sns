package com.example.sns.dto;

import com.example.sns.entity.Post;

import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
