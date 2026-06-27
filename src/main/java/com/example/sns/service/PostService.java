package com.example.sns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.dto.PostRequestDto;
import com.example.sns.dto.PostResponseDto;
import com.example.sns.entity.Post;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto){
        Post post = Post.createPost(requestDto.title(), requestDto.content());

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }

    public List<PostResponseDto> getAllPosts(){
        return postRepository.findAll().stream()
            .map(PostResponseDto::new)
            .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Long id){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        return new PostResponseDto(post);
    }
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        post.updatePost(requestDto.title(), requestDto.content());

        Post updatedPost = postRepository.save(post);
        return new PostResponseDto(updatedPost);
    }
    @Transactional
    public void deletePost(Long id){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException("삭제할 게시글을 찾을 수 없습니다."));;

        postRepository.delete(post);
    }
}
