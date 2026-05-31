package com.example.sns.controller;

import com.example.sns.dto.FollowResponse;
import com.example.sns.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/follow")
    public ResponseEntity<Void> follow(
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {

        Long followerId = (Long) httpRequest.getAttribute("userId");
        followService.follow(followerId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 언팔로우
    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollow(
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {

        Long followerId = (Long) httpRequest.getAttribute("userId");
        followService.unfollow(followerId, userId);
        return ResponseEntity.noContent().build();
    }

    // 팔로워 목록
    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    // 팔로잉 목록
    @GetMapping("/followings")
    public ResponseEntity<List<FollowResponse>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowings(userId));
    }
}