package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 조회 - user fetch join (기존)
    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.id DESC")
    List<Post> findAllWithUser();

    // 단건 조회 - user fetch join 추가
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findByIdWithUser(Long id);
}