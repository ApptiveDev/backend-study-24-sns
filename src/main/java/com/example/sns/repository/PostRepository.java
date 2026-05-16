package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 🌟 N+1 문제 해결을 위한 Fetch Join
    @Query("select p from Post p join fetch p.user")
    List<Post> findAllWithUser();
}