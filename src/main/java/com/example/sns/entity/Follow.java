package com.example.sns.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;

@Entity
@Table(
    name = "follows",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_follower_following",
            columnNames = {"follower_id", "following_id"}
        )
    }
)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    protected Follow(){

    }
    
    public static Follow createFollow(User follower, User following){
        validateUser(follower, "팔로우를 거는 사용자는 필수입니다.");
        validateUser(following, "팔로우 대상 사용자는 필수입니다.");

        if(follower.getId() != null && follower.getId().equals(following.getId())){
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }

        Follow follow = new Follow();
        follow.follower = follower;
        follow.following = following;
        return follow;
    }

    private static void validateUser(User user, String message){
        if(user == null){
            throw new IllegalArgumentException(message);
        }
    }

    public Long getId(){
        return id;
    }

    public User getFollower(){
        return follower;
    }
    public User getFollowing(){
        return following;
    }
}
