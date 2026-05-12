package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //기본적인 save(저장), findById(단건 조회), findAll(전체 조회), delete(삭제)는
    //JpaRepository가 이미 다 만들어둔다 이거 기억하셈

    //특정 유저의 Post 전부 모아보기
    List<Post> findAllByAuthorId(Long authorId);
}