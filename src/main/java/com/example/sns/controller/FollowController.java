package com.example.sns.controller;

import com.example.sns.dto.FollowActionResponse;
import com.example.sns.dto.FollowListResponse;
import com.example.sns.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/{targetUserId}")
    public FollowActionResponse follow(
            @PathVariable Long targetUserId,
            HttpServletRequest request
    ) {
        Long loginUserId = (Long) request.getAttribute("userId");

        return followService.follow(loginUserId, targetUserId);
    }

    // 언팔로우
    @DeleteMapping("/{targetUserId}")
    public FollowActionResponse unfollow(
            @PathVariable Long targetUserId,
            HttpServletRequest request
    ) {
        Long loginUserId = (Long) request.getAttribute("userId");

        return followService.unfollow(loginUserId, targetUserId);
    }

    // 특정 사용자의 팔로워 목록 조회
    @GetMapping("/{userId}/followers")
    public FollowListResponse getFollowers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }

    // 특정 사용자의 팔로잉 목록 조회
    @GetMapping("/{userId}/followings")
    public FollowListResponse getFollowings(@PathVariable Long userId) {
        return followService.getFollowings(userId);
    }
}