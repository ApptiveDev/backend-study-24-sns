package com.example.sns.repository;

import com.example.sns.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글(postId)에 달린 댓글들만 쏙쏙 찾아오는 기능
    List<Comment> findAllByPostId(Long postId);
}