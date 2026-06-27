package com.example.sns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.dto.FollowCountResponseDto;
import com.example.sns.dto.FollowResponseDto;
import com.example.sns.dto.UserResponseDto;
import com.example.sns.entity.Follow;
import com.example.sns.entity.User;
import com.example.sns.exception.FollowException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.FollowRepository;
import com.example.sns.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository){
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FollowResponseDto follow(Long loginUserId, Long targetUserId){
        if(loginUserId.equals(targetUserId)){
            throw new FollowException("자기 자신은 팔로우할 수 없습니다.");
        }
        User follower = findUserById(loginUserId);
        User following = findUserById(targetUserId);

        if(followRepository.existsByFollowerIdAndFollowingId(loginUserId, targetUserId)) {
            throw new FollowException("이미 팔로우한 사용자입니다.");
        }

        Follow follow = Follow.createFollow(follower, following);
        followRepository.save(follow);
        return new FollowResponseDto("팔로우했습니다.");
    }

    @Transactional
    public FollowResponseDto unfollow(Long loginUserId, Long targetUserId){
        Follow follow = followRepository.findByFollowerIdAndFollowingId(loginUserId, targetUserId)
            .orElseThrow(() -> new FollowException("팔로우 관계가 존재하지 않습니다."));

        followRepository.delete(follow);

        return new FollowResponseDto("팔로우를 취소했습니다.");
    }

    public List<UserResponseDto> getFollowers(Long userId){
        validateUserExists(userId);

        return followRepository.findAllByFollowingId(userId).stream()
            .map(Follow::getFollower)
            .map(UserResponseDto::from)
            .toList();
    }

    public List<UserResponseDto> getFollowings(Long userId){
        validateUserExists(userId);

        return followRepository.findAllByFollowerId(userId).stream()
            .map(Follow::getFollowing)
            .map(UserResponseDto::from)
            .toList();
    }

    public FollowCountResponseDto getFollowCount(Long userId){
        validateUserExists(userId);

        long followerCount = followRepository.countByFollowingId(userId);
        long followingCount = followRepository.countByFollowerId(userId);

        return new FollowCountResponseDto(followerCount, followingCount);
    }

    public long getFollowerCount(Long userId){
        validateUserExists(userId);
        return followRepository.countByFollowingId(userId);
    }

    public long getFollowingCount(Long userId){
        validateUserExists(userId);
        return followRepository.countByFollowerId(userId);
    }

    private User findUserById(Long userId){
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private void validateUserExists(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }
}
