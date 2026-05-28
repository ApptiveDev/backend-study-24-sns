package com.example.sns.config;

import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // 이미 데이터가 존재하면 중복 생성하지 않고 스킵 (안전장치)
        if (userRepository.count() > 0) {
            return;
        }

        // 1. 테스트용 유저 생성 (생성자 순서: email, password, name 맞춤)
        User testUser1 = new User("test1@gmail.com", "1234", "테스터1");
        User testUser2 = new User("test2@gmail.com", "1234", "테스터2");

        userRepository.save(testUser1);
        userRepository.save(testUser2);

        // 2. 테스트용 게시글 생성 (생성자 순서: title, content, user 맞춤)
        Post post1 = new Post("첫 번째 공지", "반갑습니다. 테스트 서버입니다.", testUser1);
        Post post2 = new Post("DDD 설계 테스트", "도메인 주도 설계 적용 완료!", testUser1);
        Post post3 = new Post("질문 있습니다", "게시글 수정은 어떻게 하나요?", testUser2);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        System.out.println("✅ 초기 더미 데이터 셋업 완료!");
    }
}