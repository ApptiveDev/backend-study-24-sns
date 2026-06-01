package com.example.sns.dto;

import java.util.List;

public record FollowListResponse(
        String message,
        int count,
        List<FollowResponse> data
) {
}