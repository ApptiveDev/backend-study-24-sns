package com.example.sns.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  
    private String title;     
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Post(){

    }
    public Long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public User getUser() {
        return user;
    }

    private static void validateTitle(String title){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("게시물 제목은 필수입니다.");
        }
    }

    private static void validateContent(String content){
        if(content == null || content.isBlank()){
            throw new IllegalArgumentException("게시물 내용은 필수입니다.");
        }
    }
    public static Post createPost(String title, String content){
        validateTitle(title);
        validateContent(content);

        Post post = new Post();
        post.title = title;
        post.content = content;
        return post;
    }
    public void updatePost(String title, String content){
        validateTitle(title);
        validateContent(content);

        this.title = title;
        this.content = content;
    }
}

