package com.example.sns.dto;

// 게시글을 수정해서 보낼 때 데이터를 받는 용도로 사용
public record PostUpdateRequestDto(
        String title,
        String content,
        Long userId // 어느 유저가 쓰는지 ID값만 받음
) {}
