package com.example.sns.controller;

import com.example.sns.entity.User;
import com.example.sns.repository.UserRepository;
import com.example.sns.auth.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginPageController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 로그인 화면 보여주기
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            Model model
    ) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            model.addAttribute("error", "존재하지 않는 이메일입니다.");
            return "login";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "login";
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail());

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);

        return "redirect:/home";
    }
}