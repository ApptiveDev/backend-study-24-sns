package com.example.sns.dto;

public record FollowActionResponse(
        String action,
        String message,
        Long followerId,
        String followerNickname,
        Long followingId,
        String followingNickname
) {
}