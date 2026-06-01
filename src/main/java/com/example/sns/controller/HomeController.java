package com.example.sns.controller;

import com.example.sns.auth.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JwtUtil jwtUtil;

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        String accessToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        if (accessToken == null) {
            return "redirect:/login";
        }

        Long userId = jwtUtil.getUserId(accessToken);
        String email = jwtUtil.getEmail(accessToken);

        model.addAttribute("message", "인증에 성공했습니다.");
        model.addAttribute("userId", userId);
        model.addAttribute("email", email);

        return "home";
    }
}