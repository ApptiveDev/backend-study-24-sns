package com.example.sns.post.entity;

import com.example.sns.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content) {  // 변경 로직을 엔티티 내부에 캡슐화
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 자동으로 함께 업데이트
    }

//    private Long id;
//    private Long userId;
//    private String title;
//    private String content;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;

}
