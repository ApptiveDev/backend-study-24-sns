package com.example.sns.service;

import com.example.sns.dto.FollowResponse;
import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.FollowRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    @Transactional
    public void follow(Long followerId, Long followingId) {
        // 자기 자신을 팔로우하는 경우 방지
        if (followerId.equals(followingId)) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_YOURSELF);
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new CustomException(ErrorCode.ALREADY_FOLLOWING);
        }

        followRepository.save(new Follow(follower, following));
    }

    // 언팔로우
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

    // 팔로워 목록 (나를 팔로우하는 사람들)
    public List<FollowResponse> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findFollowersByUser(user).stream()
                .map(follow -> FollowResponse.from(follow.getFollower()))
                .collect(Collectors.toList());
    }

    // 팔로잉 목록 (내가 팔로우하는 사람들)
    public List<FollowResponse> getFollowings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findFollowingsByUser(user).stream()
                .map(follow -> FollowResponse.from(follow.getFollowing()))
                .collect(Collectors.toList());
    }
}