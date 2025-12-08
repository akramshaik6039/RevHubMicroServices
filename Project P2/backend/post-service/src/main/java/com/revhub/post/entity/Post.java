package com.revhub.post.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String content;
    
    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;
    
    private String mediaType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostVisibility visibility = PostVisibility.PUBLIC;
    
    @Column(name = "author_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Integer likesCount = 0;
    
    @Column(nullable = false)
    private Integer commentsCount = 0;
    
    @Column(nullable = false)
    private Integer sharesCount = 0;
    
    @CreationTimestamp
    private LocalDateTime createdDate;
}
