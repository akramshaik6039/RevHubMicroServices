package com.revhub.notification.service;

import com.revhub.notification.entity.Notification;
import com.revhub.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }
    
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
    
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadStatusFalse(userId);
    }
    
    public void deleteNotification(String notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    public void deleteFollowRequestNotification(Long followRequestId) {
        List<Notification> notifications = notificationRepository.findByFollowRequestId(followRequestId);
        notifications.forEach(notificationRepository::delete);
    }
    
    public void createFollowNotification(Long userId, Long actorId, String actorUsername) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(actorId);
        notification.setActorUsername(actorUsername);
        notification.setType("FOLLOW");
        notification.setMessage(actorUsername + " started following you");
        notificationRepository.save(notification);
    }
    
    public void createFollowRequestNotification(Long userId, Long actorId, String actorUsername, Long followRequestId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(actorId);
        notification.setActorUsername(actorUsername);
        notification.setType("FOLLOW_REQUEST");
        notification.setMessage(actorUsername + " requested to follow you");
        notification.setFollowRequestId(followRequestId);
        notificationRepository.save(notification);
    }
    
    public void createLikeNotification(Long userId, Long actorId, String actorUsername, Long postId) {
        if (userId.equals(actorId)) {
            return;
        }
        
        List<Notification> recentNotifications = notificationRepository.findByUserIdAndTypeOrderByCreatedDateDesc(userId, "LIKE");
        boolean isDuplicate = recentNotifications.stream()
            .filter(n -> n.getPostId() != null && n.getPostId().equals(postId) && 
                        n.getActorId() != null && n.getActorId().equals(actorId))
            .findFirst()
            .isPresent();
        
        if (isDuplicate) {
            return;
        }
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(actorId);
        notification.setActorUsername(actorUsername);
        notification.setType("LIKE");
        notification.setMessage(actorUsername + " liked your post");
        notification.setPostId(postId);
        notificationRepository.save(notification);
    }
    
    public void createCommentNotification(Long userId, Long actorId, String actorUsername, Long postId, Long commentId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(actorId);
        notification.setActorUsername(actorUsername);
        notification.setType("COMMENT");
        notification.setMessage(actorUsername + " commented on your post");
        notification.setPostId(postId);
        notification.setCommentId(commentId);
        notificationRepository.save(notification);
    }
    
    public void createMentionNotification(Long userId, Long actorId, String actorUsername, Long postId, Long commentId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(actorId);
        notification.setActorUsername(actorUsername);
        notification.setType("MENTION");
        notification.setMessage(actorUsername + " mentioned you");
        notification.setPostId(postId);
        notification.setCommentId(commentId);
        notificationRepository.save(notification);
    }
    
    public void createMessageNotification(Long userId, Long actorId, String actorUsername) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(actorId);
        notification.setActorUsername(actorUsername);
        notification.setType("MESSAGE");
        notification.setMessage(actorUsername + " sent you a message");
        notificationRepository.save(notification);
    }
}
