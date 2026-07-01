package com.example.sns.dto;

import com.example.sns.entity.User;

public record FollowResponse(
        Long userId,
        String name,
        String email
) {
    public static FollowResponse from(User user) {
        return new FollowResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}