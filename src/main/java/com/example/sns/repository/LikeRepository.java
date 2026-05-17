package com.example.sns.repository;

import com.example.sns.entity.Like;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository  extends JpaRepository<Like, Long> {
    // 특정 유저가 특정 게시글에 좋아요 눌렀는지 확인
    Optional<Like> findByUserAndPost(User user, Post post);
    // 특정 게시글에 달린 좋아요 전체 목록 확인
    List<Like> findByPost(Post post);
}
