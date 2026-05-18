package com.example.sns.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.sns.dto.CommentRequestDto;
import com.example.sns.dto.CommentResponseDto;
import com.example.sns.service.CommentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    
    @PostMapping
    public CommentResponseDto createComment(@PathVariable("postId") Long postId, @RequestBody CommentRequestDto requestDto){
        return commentService.createComment(postId, requestDto);
    }

    @GetMapping
    public List<CommentResponseDto> getCommentsByPost(@PathVariable("postId") Long postId){
        return commentService.getCommentByPost(postId);
    }

    @PutMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto  requestDto){
        return commentService.updateComment(commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId){
        commentService.deleteComment(commentId);
        return "댓글이 성공적으로 삭제되었습니다.";
    }
}
