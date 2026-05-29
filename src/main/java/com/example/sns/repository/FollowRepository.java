package com.example.sns.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sns.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>{
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    boolean existsByFollowerAndFollowingId(Long followerId, Long followingId);

    List<Follow> findAllByFollowerId(Long followerId);
    List<Follow> findAllByFollowingId(Long followingId);

    long countByFollowerId(Long followerId);
    long countByFollowingId(Long followingId);
}
