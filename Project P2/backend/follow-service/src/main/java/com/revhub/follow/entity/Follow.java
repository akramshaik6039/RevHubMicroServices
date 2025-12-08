package com.revhub.follow.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
@Data
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long followerId;
    
    @Column(nullable = false)
    private Long followingId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FollowStatus status = FollowStatus.ACCEPTED;
    
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum FollowStatus {
        PENDING, ACCEPTED
    }
}
