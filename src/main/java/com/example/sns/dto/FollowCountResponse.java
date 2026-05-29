package com.example.sns.dto;

public record FollowCountResponse(
        Long userId,
        long followerCount,
        long followingCount
) {
}
