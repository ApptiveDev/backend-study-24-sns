package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest(
        @NotBlank(message = "수정할 제목을 입력해주세요.")
        String title,

        @NotBlank(message = "수정할 내용을 입력해주세요.")
        String content
) {}