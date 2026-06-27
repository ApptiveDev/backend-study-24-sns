package com.example.sns.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.dto.CommentRequestDto;
import com.example.sns.dto.CommentResponseDto;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import com.example.sns.exception.CommentNotFoundException;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostRepository;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto){
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("댓글을 작성할 게시글을 찾을 수 없습니다."));
        
        Comment comment = Comment.createComment(requestDto.content(), post);


        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);    
    }

    public List<CommentResponseDto> getCommentByPost(Long postId){
        return commentRepository.findAllByPostId(postId).stream()
            .map(CommentResponseDto::new)
            .collect(Collectors.toList());
    }
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto){
        Comment existingComment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        existingComment.updateComment(requestDto.content());
        return new CommentResponseDto(existingComment);
    }
    @Transactional
    public void deleteComment(Long commentId){
        Comment existingComment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("삭제할 댓글을 찾을 수 없습니다."));

        commentRepository.delete(existingComment);
    }
}
