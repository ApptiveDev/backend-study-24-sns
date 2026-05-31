package com.example.sns.controller;

import com.example.sns.dto.FollowResponse;
import com.example.sns.dto.UserLoginRequest;
import com.example.sns.dto.UserLoginResponse;
import com.example.sns.dto.UserSignUpRequest;
import com.example.sns.entity.User;
import com.example.sns.service.FollowService;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;
    private final FollowService followService;

    // 로그인 화면
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        try {
            UserLoginResponse response = userService.login(new UserLoginRequest(email, password));
            User user = userService.findByEmail(email);

            // 팔로워/팔로잉 목록 조회
            List<FollowResponse> followers = followService.getFollowers(user.getId());
            List<FollowResponse> followings = followService.getFollowings(user.getId());

            model.addAttribute("email", email);
            model.addAttribute("userId", user.getId());
            model.addAttribute("accessToken", response.accessToken());
            model.addAttribute("followers", followers);
            model.addAttribute("followings", followings);
            return "feed";
        } catch (Exception e) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            Model model) {

        try {
            userService.signUp(new UserSignUpRequest(email, password, name));
            return "redirect:/login"; // 성공 시 로그인 화면으로
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    // 피드 화면
    @GetMapping("/feed")
    public String feedPage() {
        return "feed";
    }
}