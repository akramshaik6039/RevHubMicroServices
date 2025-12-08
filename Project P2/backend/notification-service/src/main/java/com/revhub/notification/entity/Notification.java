package com.revhub.notification.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
public class Notification {
    @Id
    private String id;
    private Long userId;
    private Long actorId;
    private String actorUsername;
    private String actorProfilePicture;
    private String type;
    private String message;
    private Boolean readStatus = false;
    private LocalDateTime createdDate = LocalDateTime.now();
    private Long postId;
    private Long commentId;
    private Long followRequestId;
}
