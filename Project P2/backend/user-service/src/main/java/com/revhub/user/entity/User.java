package com.revhub.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String fullName;
    private String bio;
    
    @Column(columnDefinition = "LONGTEXT")
    private String profilePicture;
    
    private String coverPicture;
    
    @Column(nullable = false)
    private Boolean isPrivate = false;
    
    @Column(nullable = false)
    private Boolean isVerified = false;
    
    private String verificationOtp;
    private LocalDateTime otpExpiry;
    
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
