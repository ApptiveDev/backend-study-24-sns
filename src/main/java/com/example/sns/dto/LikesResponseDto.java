package com.example.sns.dto;

import lombok.Getter;

@Getter
public class LikesResponseDto {
    private String message;

    public LikesResponseDto(String message){
        this.message = message;
    }
}
