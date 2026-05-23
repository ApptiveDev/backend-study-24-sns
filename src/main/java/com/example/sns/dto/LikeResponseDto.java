package com.example.sns.dto;

import java.util.List;

public record LikeResponseDto(
        int likeCount, // 좋아요 누른 유저 수
        List<String> usernames  // 좋아요 누른 유저들의 이름 목록
) {}
