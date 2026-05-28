package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 유저 정보를 포함하여 모든 게시글을 내림차순으로 조회한다.
    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.id DESC")
    List<Post> findAllWithUser();
}