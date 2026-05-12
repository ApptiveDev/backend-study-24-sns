package com.example.sns.repository;

import com.example.sns.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //특정 게시글(Post)에 달린 댓글만 전부 불러오기
    List<Comment> findAllByPostId(Long postId);
}