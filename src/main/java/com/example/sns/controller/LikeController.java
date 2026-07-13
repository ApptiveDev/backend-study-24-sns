package com.example.sns.controller;

import com.example.sns.auth.CustomUserDetails;
import com.example.sns.dto.LikeResponseDto;
import com.example.sns.dto.LikeToggleResponseDto;
import com.example.sns.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 추가, 취소 (liked 여부를 응답 바디에 담아서 반환)
    @PostMapping
    public ResponseEntity<LikeToggleResponseDto> toggle(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean liked = likeService.toggleLike(postId, userDetails.getUserId());
        return ResponseEntity.ok(new LikeToggleResponseDto(liked));
    }

    // 좋아요 조회 (유저 수, 유저 목록)
    @GetMapping
    public ResponseEntity<LikeResponseDto> getLikes(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikes(postId));
    }
}
