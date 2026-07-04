package com.example.sns.controller;

import com.example.sns.auth.CustomUserDetails;
import com.example.sns.dto.*;
import com.example.sns.entity.User;
import com.example.sns.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final FollowService followService;

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
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUserId();

        User user = userService.getUserOrNull(userId);

        if (user == null) {
            return "redirect:/view/login";
        }

        FollowResponseDto followers = followService.getFollowers(userId);
        FollowResponseDto followings = followService.getFollowings(userId);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("postCount", postService.countPostsByUser(user));
        model.addAttribute("followerCount", followers.count());
        model.addAttribute("followingCount", followings.count());

        return "mypage";
    }

    // 게시글 작성 페이지
    @GetMapping("/posts/new")
    public String postWritePage(Model model) {
        model.addAttribute("postForm", new PostCreateRequestDto("", ""));
        return "post-form";
    }

    // 게시글 작성 처리
    @PostMapping("/posts")
    public String createPost(
            @ModelAttribute("postForm") @Valid PostCreateRequestDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "post-form";
        }

        try {
            postService.createPost(dto, userDetails.getUserId());
            return "redirect:/view/posts";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "post-form";
        }
    }

    // 게시글 목록
    @GetMapping("/posts")
    public String postList(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<PostResponseDto> posts = postService.getAllPosts(pageable);
        model.addAttribute("posts", posts);
        return "post-list";
    }

    // 게시글 상세 (좋아요/댓글 포함)
    @GetMapping("/posts/{id}")
    public String postDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {
        PostResponseDto post = postService.getPost(id);
        List<CommentResponseDto> comments = commentService.getComments(id);
        LikeResponseDto likes = likeService.getLikes(id);
        User currentUser = userService.getUserOrNull(userDetails.getUserId());

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("likes", likes);
        model.addAttribute("liked", likes.usernames().contains(currentUser.getUsername()));
        model.addAttribute("currentUsername", currentUser.getUsername());
        model.addAttribute("commentForm", new CommentCreateRequestDto(""));

        return "post-detail";
    }

    // 댓글 작성 (뷰에서 폼 제출)
    @PostMapping("/posts/{id}/comments")
    public String createComment(
            @PathVariable Long id,
            @ModelAttribute("commentForm") @Valid CommentCreateRequestDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (!bindingResult.hasErrors()) {
            commentService.createComment(id, dto, userDetails.getUserId());
        }
        return "redirect:/view/posts/" + id;
    }

    // 댓글 삭제 (본인만)
    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.deleteComment(commentId, userDetails.getUserId());
        return "redirect:/view/posts/" + postId;
    }

    // 좋아요 토글
    @PostMapping("/posts/{id}/likes")
    public String toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        likeService.toggleLike(id, userDetails.getUserId());
        return "redirect:/view/posts/" + id;
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
