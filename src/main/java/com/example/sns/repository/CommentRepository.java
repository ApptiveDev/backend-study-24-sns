package com.example.sns.repository;

import com.example.sns.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.post where c.post.id = :postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);
}
