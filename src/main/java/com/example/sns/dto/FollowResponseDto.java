package com.example.sns.dto;

import java.util.List;

public record FollowResponseDto(
        int count, // 팔로워/팔로잉 수
        List<FollowUserDto> users // 팔로워/팔로잉 유저 목록 (id + 이름)
) {}
