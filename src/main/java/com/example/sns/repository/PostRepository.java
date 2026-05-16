package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// JPA 버전
// JpaRepository를 상속하므로 @Repository 생략 가능
public interface PostRepository extends JpaRepository<Post, Long> {
    // save(), findAll(), findById(), deleteById() 등이 자동으로 생성.
}
