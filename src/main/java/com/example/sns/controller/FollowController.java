package com.example.sns.controller;

import com.example.sns.auth.AuthInterceptor;
import com.example.sns.dto.FollowCountResponse;
import com.example.sns.dto.UserResponse;
import com.example.sns.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> follow(
            @PathVariable Long userId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        followService.follow(userId, authUserId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<Void> unfollow(
            @PathVariable Long userId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        followService.unfollow(userId, authUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserResponse>> findFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.findFollowers(userId));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<UserResponse>> findFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.findFollowings(userId));
    }

    @GetMapping("/{userId}/follow-count")
    public ResponseEntity<FollowCountResponse> count(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.count(userId));
    }
}
