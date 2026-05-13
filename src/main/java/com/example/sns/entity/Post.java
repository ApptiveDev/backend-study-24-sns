package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // @ManyToOne을 써서 이 게시글이 어떤 유저(User 객체)가 쓴 건지 직접 연결합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 실제 DB 테이블에는 'user_id'라는 이름으로 저장됩니다.
    private User user;
}