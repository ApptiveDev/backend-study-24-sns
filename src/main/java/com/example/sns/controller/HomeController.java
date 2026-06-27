package com.example.sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import com.example.sns.entity.User;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.UserRepository;
import com.example.sns.service.FollowService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserRepository userRepository;
    private final FollowService followService;

    @GetMapping("/home")
    public String home(
        @RequestAttribute("loginUserId") Long loginUserId,
        Model model
    ){
        User loginUser = userRepository.findById(loginUserId)
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        long followerCount = followService.getFollowerCount(loginUserId);
        long followingCount = followService.getFollowingCount(loginUserId);

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followingCount",followingCount);

        return "home";
    }
}
