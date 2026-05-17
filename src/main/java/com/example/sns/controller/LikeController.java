package com.example.sns.controller;

import com.example.sns.dto.LikeCountResponse;
import com.example.sns.dto.LikeCreateRequest;
import com.example.sns.dto.LikeResponse;
import com.example.sns.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 좋아요 누르기
    @PostMapping("/likes")
    public ResponseEntity<LikeResponse> createLike(@RequestBody LikeCreateRequest request) {
        LikeResponse response = likeService.createLike(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 좋아요 전체 조회
    @GetMapping("/likes")
    public ResponseEntity<List<LikeResponse>> getLikes() {
        List<LikeResponse> response = likeService.getLikes();
        return ResponseEntity.ok(response);
    }

    // 좋아요 단건 조회
    @GetMapping("/likes/{likeId}")
    public ResponseEntity<LikeResponse> getLike(@PathVariable Long likeId) {
        LikeResponse response = likeService.getLike(likeId);
        return ResponseEntity.ok(response);
    }

    // 특정 게시글 좋아요 목록 조회
    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<List<LikeResponse>> getLikesByPost(@PathVariable Long postId) {
        List<LikeResponse> response = likeService.getLikesByPost(postId);
        return ResponseEntity.ok(response);
    }

    // 특정 게시글 좋아요 개수 조회
    @GetMapping("/posts/{postId}/likes/count")
    public ResponseEntity<LikeCountResponse> getLikeCountByPost(@PathVariable Long postId) {
        LikeCountResponse response = likeService.getLikeCountByPost(postId);
        return ResponseEntity.ok(response);
    }

    // 좋아요 취소
    @DeleteMapping("/likes/{likeId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long likeId) {
        likeService.deleteLike(likeId);
        return ResponseEntity.noContent().build();
    }

    // 사용자와 게시글 기준으로 좋아요 취소
    @DeleteMapping("/posts/{postId}/likes/users/{userId}")
    public ResponseEntity<Void> deleteLikeByUserAndPost(
            @PathVariable Long postId,
            @PathVariable Long userId
    ) {
        likeService.deleteLikeByUserAndPost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}