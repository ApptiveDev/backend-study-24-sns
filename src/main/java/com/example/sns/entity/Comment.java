package com.example.sns.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    protected Comment(){
    }

    
    public Long getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public Post getPost(){
        return post;
    }

    public User getUser(){
        return user;
    }
    private static void validateContent(String content){
        if(content == null || content.isBlank()){
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }
    }
    private static void validatePost(Post post){
        if(post == null){
            throw new IllegalArgumentException("게시글은 필수입니다.");
        }
    }
    
    public static Comment createComment(String content, Post post){
        validateContent(content);
        validatePost(post);

        Comment comment = new Comment();
        comment.content = content;
        comment.post = post;
        return comment;
    }

    public void updateComment(String content){
        validateContent(content);
        this.content = content;
    }
}
