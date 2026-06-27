package com.example.sns.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    protected Likes(){

    }
    public Long getId(){
        return id;
    }

    public User getUser(){
        return user;
    }

    public Post getPost(){
        return post;
    }
    public static Likes createLike(User user, Post post){
        validateUser(user);
        validatePost(post);

        Likes like = new Likes();
        like.user = user;
        like.post = post;
        return like;
    }

    private static void validateUser(User user){
        if(user == null){
            throw new IllegalArgumentException("사용자는 필수입니다.");
        }
    }

    private static void validatePost(Post post){
        if(post == null){
            throw new IllegalArgumentException("게시글은 필수입니다.");
        }
    }
}
