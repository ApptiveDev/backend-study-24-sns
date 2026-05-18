package com.example.sns.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class LikesRequestDto {
    @NotNull(message = "사용자 ID는 필수입니다.")
    @Positive(message = "사용자 ID는 양수이여야 합니다.")
    private Long userId;
}
