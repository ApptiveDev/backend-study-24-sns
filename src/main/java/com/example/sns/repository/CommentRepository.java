package com.example.sns.repository;

import com.example.sns.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // postId + commentId로 댓글 조회 (소속 검증 포함)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post WHERE c.id = :commentId AND c.post.id = :postId")
    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId")
    List<Comment> findAllByPostId(Long postId);
}