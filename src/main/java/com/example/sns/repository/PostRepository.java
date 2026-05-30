package com.example.sns.repository;

import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// JPA 버전
// JpaRepository를 상속하므로 @Repository 생략 가능
public interface PostRepository extends JpaRepository<Post, Long> {
    // save(), findAll(), findById(), deleteById() 등이 자동으로 생성.

    @Query("SELECT p FROM Post p JOIN FETCH p.user")
    List<Post> findAllWithUser();

    long countByUser(User user);
}
