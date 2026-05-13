package com.example.sns.controller;

import com.example.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> toggle(@PathVariable Long postId, @RequestParam Long userId) {
        likeService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }
}
