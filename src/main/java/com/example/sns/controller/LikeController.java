package com.example.sns.controller;

import com.example.sns.auth.AuthInterceptor;
import com.example.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> addLike(
            @PathVariable Long postId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        likeService.addLike(postId, authUserId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeLike(
            @PathVariable Long postId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        likeService.removeLike(postId, authUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }
}
