package com.example.sns.follow.repository;

import com.example.sns.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 특정 사용자를 팔로우했는지 확인
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 팔로우 관계 찾기
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 특정 사용자의 팔로워 목록 (follower를 fetch join)
    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.following.id = :followingId")
    List<Follow> findByFollowingId(@Param("followingId") Long followingId);

    // 특정 사용자의 팔로잉 목록 (following을 fetch join)
    @Query("SELECT f FROM Follow f JOIN FETCH f.following WHERE f.follower.id = :followerId")
    List<Follow> findByFollowerId(@Param("followerId") Long followerId);

    long countByFollowerId(Long followerId);

    long countByFollowingId(Long followingId);
}
