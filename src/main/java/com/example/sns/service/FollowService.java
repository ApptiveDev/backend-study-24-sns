package com.example.sns.service;

import com.example.sns.dto.FollowCountResponse;
import com.example.sns.dto.UserResponse;
import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import com.example.sns.exception.BadRequestException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.NotFoundException;
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

    @Transactional
    public void follow(Long targetUserId, Long authUserId) {
        if (targetUserId.equals(authUserId)) {
            throw new BadRequestException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        User follower = getUser(authUserId);
        User following = getUser(targetUserId);

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new BadRequestException(ErrorCode.FOLLOW_ALREADY_EXISTS);
        }

        followRepository.save(Follow.create(follower, following));
    }

    @Transactional
    public void unfollow(Long targetUserId, Long authUserId) {
        User follower = getUser(authUserId);
        User following = getUser(targetUserId);

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

    public List<UserResponse> findFollowers(Long userId) {
        User user = getUser(userId);
        return followRepository.findAllByFollowing(user)
                .stream()
                .map(Follow::getFollower)
                .map(UserResponse::from)
                .toList();
    }

    public List<UserResponse> findFollowings(Long userId) {
        User user = getUser(userId);
        return followRepository.findAllByFollower(user)
                .stream()
                .map(Follow::getFollowing)
                .map(UserResponse::from)
                .toList();
    }

    public FollowCountResponse count(Long userId) {
        User user = getUser(userId);
        return new FollowCountResponse(
                user.getId(),
                followRepository.countByFollowing(user),
                followRepository.countByFollower(user)
        );
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
