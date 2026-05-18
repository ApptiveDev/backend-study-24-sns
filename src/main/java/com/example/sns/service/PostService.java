package com.example.sns.service;

import org.springframework.stereotype.Service;

import com.example.sns.dto.PostRequestDto;
import com.example.sns.dto.PostResponseDto;
import com.example.sns.entity.Post;
import com.example.sns.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto){
        Post post = new Post();
        post.setTitle(requestDto.title());
        post.setContent(requestDto.content());

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
            .orElseThrow(() -> new IllegalArgumentException("잘못된 번호 입니다."));
        return new PostResponseDto(post);
    }

    public PostResponseDto updatePost(Long id, PostRequestDto requestDto){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 번호입니다."));

        post.setTitle(requestDto.title());
        post.setContent(requestDto.content());

        Post updatedPost = postRepository.save(post);
        return new PostResponseDto(updatedPost);
    }

    public void deletePost(Long id){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 게시물이 없습니다."));

        postRepository.delete(post);
    }
}
