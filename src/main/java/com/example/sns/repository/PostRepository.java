package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.author")
    List<Post> findAllWithAuthor();

    @Query("""
            SELECT DISTINCT p
            FROM Post p
            JOIN FETCH p.author
            LEFT JOIN FETCH p.comments c
            LEFT JOIN FETCH c.author
            WHERE p.id = :postId
            """)
    Optional<Post> findWithDetailsById(@Param("postId") Long postId);
}