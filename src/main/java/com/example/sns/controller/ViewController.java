package com.example.sns.controller;

import com.example.sns.dto.FollowResponseDto;
import com.example.sns.dto.LoginRequestDto;
import com.example.sns.dto.LoginResponseDto;
import com.example.sns.dto.UserRequestDto;
import com.example.sns.entity.User;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import com.example.sns.service.FollowService;
import com.example.sns.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FollowService followService;
    private final PostRepository postRepository;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginRequestDto("", ""));
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginForm") @Valid LoginRequestDto dto,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            LoginResponseDto tokens = userService.login(dto);

            Cookie cookie = new Cookie("accessToken", tokens.accessToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            return "redirect:/view/mypage";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new UserRequestDto("", "", ""));
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(
            @ModelAttribute("registerForm") @Valid UserRequestDto dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(dto);
            return "redirect:/view/login";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    // 마이페이지
    @GetMapping("/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return "redirect:/view/login";
        }

        FollowResponseDto followers = followService.getFollowers(userId);
        FollowResponseDto followings = followService.getFollowings(userId);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("postCount", postRepository.countByUser(user));
        model.addAttribute("followerCount", followers.count());
        model.addAttribute("followingCount", followings.count());

        return "mypage";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/view/login";
    }
}
