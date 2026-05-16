package com.example.sns.repository;

import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostOrderByCreatedAtAsc(Post post);
}
