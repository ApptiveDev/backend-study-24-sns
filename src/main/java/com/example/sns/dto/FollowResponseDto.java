package com.example.sns.dto;

import java.util.List;

public record FollowResponseDto(
        int count, // 팔로워/팔로잉 수
        List<String> usernames // 팔로워/팔로잉 유저명 목록
) {}
