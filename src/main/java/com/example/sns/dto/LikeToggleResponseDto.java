package com.example.sns.dto;

public record LikeToggleResponseDto(
        boolean liked // true: 좋아요 추가됨, false: 좋아요 취소됨
) {}
