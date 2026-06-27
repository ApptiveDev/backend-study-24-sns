package com.example.sns.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sns.dto.FollowCountResponseDto;
import com.example.sns.dto.FollowResponseDto;
import com.example.sns.dto.UserResponseDto;
import com.example.sns.service.FollowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<FollowResponseDto> follow(
        @RequestAttribute("loginUserId") Long loginUserId,
        @PathVariable("targetUserId") Long targetUserId
    ){
        FollowResponseDto responseDto = followService.follow(loginUserId, targetUserId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{targetUserId}/follow")
    public ResponseEntity<FollowResponseDto> unfollow(
        @RequestAttribute("loginUserId") Long loginUserId,
        @PathVariable("targetUserId") Long targetUserId
    ){
        FollowResponseDto responseDto = followService.unfollow(loginUserId, targetUserId);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserResponseDto>> getFollowers(@PathVariable("userId") Long userId){
        List<UserResponseDto> responseDtoList = followService.getFollowers(userId);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<UserResponseDto>> getFollowings(@PathVariable("userId") Long userId){
        List<UserResponseDto> responseDtoList = followService.getFollowings(userId);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/{userId}/follow-count")
    public ResponseEntity<FollowCountResponseDto> getFollowCount(@PathVariable("userId") Long userId){
        FollowCountResponseDto responseDto = followService.getFollowCount(userId);
        return ResponseEntity.ok(responseDto);
    }
}
