package com.example.sns.config;

import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import com.example.sns.entity.Likes;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.LikesRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {
    
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        if(userRepository.count()>0 || postRepository.count() > 0){
            log.info("==== 더미 데이터가 이미 존재해서 생성을 건너뜁니다 ====");
            return;
        }

        log.info("==== 더미 데이터 생성을 시작합니다 ====");

        User user1 = User.createUser(
            "user1",
            "user1@test.com",
            passwordEncoder.encode("1234")
        );

        User user2 = User.createUser(
            "user2",
            "user2@test.com",
            passwordEncoder.encode("1234")
        );

        List<User> users = userRepository.saveAll(List.of(user1,user2));

        Post post1 = Post.createPost("테스트 게시글 1", "좋아요 테스트용 게시글입니다.");
        Post post2 = Post.createPost("테스트 게시글 2", "두 번째 테스트 게시글입니다.");

        List<Post> posts = postRepository.saveAll(List.of(post1, post2));

        Likes like = Likes.createLike(users.get(0), posts.get(0));
        likesRepository.save(like);

        log.info("==== 더미 데이터 생성이 완료되었습니다 ====");
        log.info("테스트 로그인 계정: username=user1, password=1234");
        log.info("user1 id: {}, user2 id: {}", users.get(0).getId(), users.get(1).getId());
        log.info("post1 id: {}, post2 id: {}",posts.get(0).getId(), posts.get(1).getId());
    }
}
