package com.example.sns.dto;

import com.example.sns.entity.User;

public record FollowResponse(
        Long userId,
        String nickname
) {
    public FollowResponse(User user) {
        this(
                user.getId(),
                user.getNickname()
        );
    }
}