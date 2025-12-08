package com.revhub.post.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String content;
    
    @Column(name = "author_id", nullable = false)
    private Long userId;
    
    @Column(name = "post_id", nullable = false)
    private Long postId;
    
    @Column(name = "parent_comment_id")
    private Long parentCommentId;
    
    @CreationTimestamp
    private LocalDateTime createdDate;
    
    @Transient
    private List<Comment> replies;
}
