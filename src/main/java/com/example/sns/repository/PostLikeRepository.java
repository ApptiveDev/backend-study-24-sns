package com.example.sns.repository;

import com.example.sns.entity.Post;
import com.example.sns.entity.PostLike;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}