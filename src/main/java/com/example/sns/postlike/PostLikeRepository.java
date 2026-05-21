package com.example.sns.postlike;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostLikeRepository extends CrudRepository<PostLike, Long> {

    // 중복 체크
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    // 좋아요 취소
    void deleteByUserIdAndPostId(Long userId, Long postId);

    // 게시물의 좋아요 수
    // fetch join 안해도 되는가?
    long countByPostId(Long postId);

    // 내가 좋아요 누른 게시물들
    // fetch join 안해도 되는가?
    List<PostLike> findByUserId(Long userId);
}
