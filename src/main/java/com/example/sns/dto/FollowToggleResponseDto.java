package com.example.sns.dto;

public record FollowToggleResponseDto(
        boolean following // true: 팔로우됨, false: 언팔로우됨
) {}
