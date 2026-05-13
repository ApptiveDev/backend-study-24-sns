package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<관리할 엔티티, 그 엔티티의 PK 타입>
public interface PostRepository extends JpaRepository<Post, Long> {
}