package com.example.sns.repository;

import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);
    //: 이미 팔로우했는지 확인
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    // 언팔로우할 때 기존 팔로우 관계 찾기
    List<Follow> findAllByFollower(User follower);
    // 내가 팔로우한 사람들 조회
    List<Follow> findAllByFollowing(User following);
    //나를 팔로우한 사람들 조회
    long countByFollower(User follower);
    //내가 팔로우한 수
    long countByFollowing(User following);
    //나를 팔로우한 수
}
