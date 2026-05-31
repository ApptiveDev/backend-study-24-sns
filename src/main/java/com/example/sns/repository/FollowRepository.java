package com.example.sns.repository;

import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 이미 팔로우 중인지 확인
    boolean existsByFollowerAndFollowing(User follower, User following);

    // 팔로우 관계 조회 (언팔로우용)
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // 팔로워 목록 (나를 팔로우하는 사람들)
    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.following = :user")
    List<Follow> findFollowersByUser(User user);

    // 팔로잉 목록 (내가 팔로우하는 사람들)
    @Query("SELECT f FROM Follow f JOIN FETCH f.following WHERE f.follower = :user")
    List<Follow> findFollowingsByUser(User user);
}