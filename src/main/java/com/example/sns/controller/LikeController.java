package com.example.sns.controller;

import com.example.sns.dto.LikeCountResponse;
import com.example.sns.service.LikeCommandService;
import com.example.sns.service.LikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/likes")
public class LikeController {
    private final LikeCommandService likeCommandService;
    private final LikeQueryService likeQueryService;

    @PostMapping
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        likeCommandService.likePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        likeCommandService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<LikeCountResponse> countLikes(
            @PathVariable Long postId
    ) {
        long likeCount = likeQueryService.countLikes(postId);
        return ResponseEntity.ok(new LikeCountResponse(postId, likeCount));
    }
}
