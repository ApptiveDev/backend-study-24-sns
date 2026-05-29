package com.example.sns.controller;

import com.example.sns.dto.FollowResponseDto;
import com.example.sns.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우 / 언팔로우: POST /api/users/2/follow
    @PostMapping("/follow")
    public ResponseEntity<Void> toggleFollow(
            @PathVariable Long userId,          // 팔로우 대상
            HttpServletRequest request           // JWT에서 꺼낸 본인 ID
    ) {
        Long myId = (Long) request.getAttribute("userId");
        followService.toggleFollow(userId, myId);
        return ResponseEntity.ok().build();
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
