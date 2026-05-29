package com.example.sns.follow.controller;

import com.example.sns.follow.dto.FollowStatusResponse;
import com.example.sns.follow.service.FollowService;
import com.example.sns.security.annotation.LoginUserId;
import com.example.sns.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping
    public ResponseEntity<Void> follow(@LoginUserId Long followerId,
                                       @PathVariable Long userId
                                       ) {
        followService.follow(followerId, userId);
        return ResponseEntity.status(201).build();
    }

    // 언팔로우
    @DeleteMapping
    public ResponseEntity<Void> unfollow(@PathVariable Long userId,
                                         @LoginUserId Long followerId
    ) {
        followService.unfollow(followerId, userId);
        return ResponseEntity.noContent().build();
    }


    // 팔로우 여부 확인
    // 언제 필요?
    @GetMapping("/status")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long userId,
            @LoginUserId Long followerId
    ) {
        boolean following = followService.isFollowing(followerId, userId);
        return ResponseEntity.ok(following);
    }

    // 팔로워 목록
    @GetMapping("/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long userId) {
        List<UserResponse> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    // 팔로잉 목록
    @GetMapping("/followings")
    public ResponseEntity<List<UserResponse>> getFollowings(@PathVariable Long userId) {
        List<UserResponse> followings = followService.getFollowings(userId);
        return ResponseEntity.ok(followings);
    }

    // 팔로워/팔로잉 수
    @GetMapping("/stats")
    public ResponseEntity<FollowStatusResponse> getFollowStats(@PathVariable Long userId) {
        long followers = followService.getFollowerCount(userId);
        long followings = followService.getFollowingCount(userId);
        return ResponseEntity.ok(new FollowStatusResponse(followers, followings));
    }
}
