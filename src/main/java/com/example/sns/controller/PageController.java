package com.example.sns.controller;

import com.example.sns.dto.UserLoginRequest;
import com.example.sns.dto.UserLoginResponse;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller  // @RestController 아님! HTML 뷰를 반환하는 컨트롤러
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;

    // 로그인 화면 반환
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html 을 찾아서 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        try {
            UserLoginResponse response = userService.login(new UserLoginRequest(email, password));
            model.addAttribute("accessToken", response.accessToken());
            model.addAttribute("email", email);
            return "feed"; // 성공 시 templates/feed.html 반환
        } catch (Exception e) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login"; // 실패 시 로그인 화면으로 다시
        }
    }

    // 피드 화면 반환
    @GetMapping("/feed")
    public String feedPage() {
        return "feed";
    }
}