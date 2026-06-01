package com.example.sns.service;

import com.example.sns.dto.FollowActionResponse;
import com.example.sns.dto.FollowListResponse;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    @Transactional
    public FollowActionResponse follow(Long loginUserId, Long targetUserId) {
        User follower = findUserById(loginUserId);
        User following = findUserById(targetUserId);

        // 자기 자신 팔로우 방지
        if (follower.getId().equals(following.getId())) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        // 중복 팔로우 방지
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new CustomException(ErrorCode.ALREADY_FOLLOWED);
        }

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        return new FollowActionResponse(
                "FOLLOW",
                follower.getNickname() + "님이 " + following.getNickname() + "님을 팔로우했습니다.",
                follower.getId(),
                follower.getNickname(),
                following.getId(),
                following.getNickname()
        );
    }

    // 언팔로우
    @Transactional
    public FollowActionResponse unfollow(Long loginUserId, Long targetUserId) {
        User follower = findUserById(loginUserId);
        User following = findUserById(targetUserId);

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);

        return new FollowActionResponse(
                "UNFOLLOW",
                follower.getNickname() + "님이 " + following.getNickname() + "님을 언팔로우했습니다.",
                follower.getId(),
                follower.getNickname(),
                following.getId(),
                following.getNickname()
        );
    }

    // 팔로워 목록 조회
    public FollowListResponse getFollowers(Long userId) {
        User user = findUserById(userId);

        List<FollowResponse> followers = followRepository.findAllByFollowing(user)
                .stream()
                .map(follow -> new FollowResponse(follow.getFollower()))
                .toList();

        String message = followers.isEmpty()
                ? "팔로워 목록이 없습니다."
                : "팔로워 목록 조회 성공";

        return new FollowListResponse(
                message,
                followers.size(),
                followers
        );
    }

    // 팔로잉 목록 조회
    public FollowListResponse getFollowings(Long userId) {
        User user = findUserById(userId);

        List<FollowResponse> followings = followRepository.findAllByFollower(user)
                .stream()
                .map(follow -> new FollowResponse(follow.getFollowing()))
                .toList();

        String message = followings.isEmpty()
                ? "팔로잉 목록이 없습니다."
                : "팔로잉 목록 조회 성공";

        return new FollowListResponse(
                message,
                followings.size(),
                followings
        );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}