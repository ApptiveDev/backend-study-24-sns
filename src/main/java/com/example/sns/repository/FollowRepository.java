package com.example.sns.repository;

import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 이미 팔로우했는지 확인
    boolean existsByFollowerAndFollowing(User follower, User following);

    // 언팔로우할 때 팔로우 관계 찾기
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // 특정 사용자를 팔로우하는 사람들 목록
    List<Follow> findAllByFollowing(User following);

    // 특정 사용자가 팔로우하고 있는 사람들 목록
    List<Follow> findAllByFollower(User follower);
}