package com.example.sns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sns.service.LikesService;

import lombok.RequiredArgsConstructor;

import com.example.sns.dto.LikesRequestDto;
import com.example.sns.dto.LikesResponseDto;


@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping
    public LikesResponseDto toggleLike(@PathVariable("postId") Long postId, @RequestBody LikesRequestDto requestDto){
        return likesService.toggleLike(postId, requestDto);
    }

    @GetMapping("/count")
    public int getLikesCount(@PathVariable("postId") Long postId){
        return likesService.getLikesCount(postId);
    }
}
