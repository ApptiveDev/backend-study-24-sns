package com.example.sns.repository;

import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // JPA가 자동으로 아래 SQL로 변환
    List<Comment> findByPost(Post post); // 특정 게시물 댓글 확인
}
