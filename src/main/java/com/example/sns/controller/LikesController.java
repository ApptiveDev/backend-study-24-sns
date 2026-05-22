package com.example.sns.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sns.service.LikesService;

import lombok.RequiredArgsConstructor;

import com.example.sns.dto.LikesResponseDto;


@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<LikesResponseDto> toggleLike(@PathVariable("postId") Long postId, @RequestAttribute("loginUserId") Long loginUserId){
        LikesResponseDto responseDto = likesService.toggleLike(postId, loginUserId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getLikesCount(@PathVariable("postId") Long postId){
        //처음에 ResponseEntity<int>라 했는데 자꾸 오류나서 찾아보니 Integer을 써야한다고 되어있었습니다
        //왜 int를 사용할수 없고 Integer을 사용해야하는지 궁금합니다.
        int count = likesService.getLikesCount(postId);
        return ResponseEntity.ok(count);
    }
}
