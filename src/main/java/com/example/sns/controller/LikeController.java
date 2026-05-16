package com.example.sns.controller;

import com.example.sns.dto.LikeResponseDto;
import com.example.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 추가, 취소
    @PostMapping
    public ResponseEntity<Void> toggle(@PathVariable Long postId, @RequestParam Long userId) {
        likeService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 좋아요 조회 (유저 수, 유저 목록)
    @GetMapping
    public ResponseEntity<LikeResponseDto> getLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikes(postId));
    }
}
