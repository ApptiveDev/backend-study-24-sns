package com.example.sns.controller;

import com.example.sns.auth.CustomUserDetails;
import com.example.sns.dto.FollowResponseDto;
import com.example.sns.dto.FollowToggleResponseDto;
import com.example.sns.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우, 언팔로우: POST /api/users/2/follow
    // (following 여부를 응답 바디에 담아서 반환)
    @PostMapping("/follow")
    public ResponseEntity<FollowToggleResponseDto> toggleFollow(
            @PathVariable Long userId,          // 팔로우 대상
            @AuthenticationPrincipal CustomUserDetails userDetails           // JWT에서 꺼낸 본인 ID
    ) {
        boolean following = followService.toggleFollow(userId, userDetails.getUserId());
        return ResponseEntity.ok(new FollowToggleResponseDto(following));
    }

    // 팔로워 목록 조회: GET /api/users/2/followers
    @GetMapping("/followers")
    public ResponseEntity<FollowResponseDto> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    // 팔로잉 목록 조회: GET /api/users/2/followings
    @GetMapping("/followings")
    public ResponseEntity<FollowResponseDto> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowings(userId));
    }
}
