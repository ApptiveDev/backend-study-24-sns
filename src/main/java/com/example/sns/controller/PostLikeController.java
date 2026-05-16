package com.example.sns.controller;

import com.example.sns.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<String> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        String resultMessage = postLikeService.toggleLike(postId, userId);
        return ResponseEntity.ok(resultMessage);
    }
}