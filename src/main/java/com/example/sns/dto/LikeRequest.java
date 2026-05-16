package com.example.sns.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequest {

    @NotNull(message = "userId is required.")
    private Long userId;
}
