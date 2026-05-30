package com.example.sns.repository;

import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Fetch Join으로 N+1 문제 해결
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post = :post")
    List<Comment> findByPostWithUser(@Param("post") Post post);
}

// 기존 방식 (N+1 문제)
//-- 1번 쿼리: 댓글 3개 가져옴
//SELECT * FROM comments WHERE post_id = 1
//-- 결과: 댓글은 가져왔는데 user 정보는 없음
//-- comment.getUser().getUsername() 호출하는 순간 추가 쿼리 발생
//-- 2번 쿼리: 1번 댓글 작성자
//SELECT * FROM users WHERE user_id = 1  -- testuser1
//-- 3번 쿼리: 2번 댓글 작성자
//SELECT * FROM users WHERE user_id = 2  -- testuser2
//-- 4번 쿼리: 3번 댓글 작성자
//SELECT * FROM users WHERE user_id = 3  -- testuser3

// Fetch Join
//-- 1번 쿼리: 댓글 + 유저 한번에 가져옴
//SELECT c.*, u.* FROM comments c
//INNER JOIN users u ON c.user_id = u.user_id
//WHERE c.post_id = 1
