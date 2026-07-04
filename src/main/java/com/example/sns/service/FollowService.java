package com.example.sns.service;

import com.example.sns.dto.FollowResponseDto;
import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import com.example.sns.exception.SelfFollowException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.FollowRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우, 언팔로우 토글
    @Transactional
    public boolean toggleFollow(Long followingId, Long followerId) {
        // 자기 자신은 팔로우 불가
        if (followerId.equals(followingId)) {
            throw new SelfFollowException();
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException(followerId));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException(followingId));

        Optional<Follow> existing = followRepository.findByFollowerAndFollowing(follower, following);

        if (existing.isPresent()) {
            followRepository.delete(existing.get()); // 언팔로우
            return false;
        }
        else {
            followRepository.save(Follow.create(follower, following)); // 팔로우
            return true;
        }
    }

    // 팔로워 목록 조회 (특정 유저를 팔로우하는 사람들)
    public FollowResponseDto getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Follow> followers = followRepository.findFollowersByUser(user);
        List<String> usernames = followers.stream()
                .map(f -> f.getFollower().getUsername())
                .toList();

        return new FollowResponseDto(usernames.size(), usernames);
    }

    // 팔로잉 목록 조회 (특정 유저가 팔로우하는 사람들)
    public FollowResponseDto getFollowings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Follow> followings = followRepository.findFollowingsByUser(user);
        List<String> usernames = followings.stream()
                .map(f -> f.getFollowing().getUsername())
                .toList();

        return new FollowResponseDto(usernames.size(), usernames);
    }

    // 내가(followerId) 특정 유저(followingId)를 팔로우 중인지 확인
    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException(followerId));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException(followingId));

        return followRepository.findByFollowerAndFollowing(follower, following).isPresent();
    }
}
