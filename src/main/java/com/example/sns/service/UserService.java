package com.example.sns.service;

import com.example.sns.entity.User;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용으로 설정하여 성능 최적화
public class UserService {

    private final UserRepository userRepository;

    @Transactional // 데이터를 저장하므로 쓰기 권한 부여
    public User createUser(String name, String email) {

        // 🌟 수정된 부분: Setter 대신 생성자를 이용해 한 번에, 안전하게 조립합니다!
        User user = new User(name, email);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. ID: " + id));
    }
}