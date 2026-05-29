package com.example.sns.follow.service;

import com.example.sns.exception.BusinessException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.follow.entity.Follow;
import com.example.sns.follow.repository.FollowRepository;
import com.example.sns.user.dto.UserResponse;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("이미 팔로우했습니다.");
        }

        Follow follow = Follow.create(follower, following);

        followRepository.save(follow);
    }


    // 언팔로우
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId).orElseThrow(
                () -> new IllegalArgumentException("팔로우 관계가 없습니다."));
        followRepository.delete(follow);  // Follow 엔티티 삭제
    }


    // 팔로우 여부 확인
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }


    // 나를 팔로우하는 사람 목록 (팔로워)
    public List<UserResponse> getFollowers(Long userId) {
        return followRepository.findByFollowingId(userId)  // userId를 팔로우한 레코드들
                .stream()
                .map(follow -> UserResponse.from(follow.getFollower()))  // 팔로워 반환
                .toList();
    }


    // 내가 팔로우하는 사람 목록 (팔로잉)
    public List<UserResponse> getFollowings(Long userId) {
        return followRepository.findByFollowerId(userId)  // userId가 팔로우한 레코드들
                .stream()
                .map(follow -> UserResponse.from(follow.getFollowing()))  // 팔로잉 대상 반환
                .toList();
    }

    // 내가 팔로우하는 사람 수
    public long getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    // 나를 팔로우하는 사람 수
    public long getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
}
