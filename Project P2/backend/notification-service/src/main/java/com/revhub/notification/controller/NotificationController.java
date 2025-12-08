package com.revhub.notification.controller;

import com.revhub.notification.entity.Notification;
import com.revhub.notification.service.NotificationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {
    
    private final NotificationService notificationService;
    private final RestTemplate restTemplate;
    
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
    
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/follow-request/{followRequestId}")
    public ResponseEntity<Void> deleteFollowRequestNotification(@PathVariable Long followRequestId) {
        notificationService.deleteFollowRequestNotification(followRequestId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/follow-request/{followId}/accept")
    public ResponseEntity<String> acceptFollowRequest(@PathVariable Long followId, @RequestHeader("X-User-Id") Long userId) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity("http://follow-service/api/follows/requests/" + followId + "/accept", entity, String.class);
            notificationService.deleteFollowRequestNotification(followId);
            return ResponseEntity.ok(response.getBody());
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.err.println("Follow service error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Error accepting follow request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to accept follow request: " + e.getMessage());
        }
    }
    
    @PostMapping("/follow-request/{followId}/reject")
    public ResponseEntity<String> rejectFollowRequest(@PathVariable Long followId, @RequestHeader("X-User-Id") Long userId) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
            restTemplate.postForEntity("http://follow-service/api/follows/requests/" + followId + "/reject", entity, String.class);
            notificationService.deleteFollowRequestNotification(followId);
            return ResponseEntity.ok("Follow request rejected");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to reject follow request: " + e.getMessage());
        }
    }
    
    @PostMapping("/follow")
    public ResponseEntity<Void> notifyFollow(@RequestBody NotifyRequest request) {
        notificationService.createFollowNotification(request.getUserId(), request.getActorId(), request.getActorUsername());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/follow-request")
    public ResponseEntity<Void> notifyFollowRequest(@RequestBody FollowRequestNotifyRequest request) {
        notificationService.createFollowRequestNotification(request.getUserId(), request.getActorId(), request.getActorUsername(), request.getFollowRequestId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/like")
    public ResponseEntity<Void> notifyLike(@RequestBody PostNotifyRequest request) {
        notificationService.createLikeNotification(request.getUserId(), request.getActorId(), request.getActorUsername(), request.getPostId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/comment")
    public ResponseEntity<Void> notifyComment(@RequestBody CommentNotifyRequest request) {
        notificationService.createCommentNotification(request.getUserId(), request.getActorId(), request.getActorUsername(), request.getPostId(), request.getCommentId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/mention")
    public ResponseEntity<Void> notifyMention(@RequestBody CommentNotifyRequest request) {
        notificationService.createMentionNotification(request.getUserId(), request.getActorId(), request.getActorUsername(), request.getPostId(), request.getCommentId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/message")
    public ResponseEntity<Void> notifyMessage(@RequestBody NotifyRequest request) {
        notificationService.createMessageNotification(request.getUserId(), request.getActorId(), request.getActorUsername());
        return ResponseEntity.ok().build();
    }
    
    @Data
    static class NotifyRequest {
        private Long userId;
        private Long actorId;
        private String actorUsername;
    }
    
    @Data
    static class FollowRequestNotifyRequest {
        private Long userId;
        private Long actorId;
        private String actorUsername;
        private Long followRequestId;
    }
    
    @Data
    static class PostNotifyRequest {
        private Long userId;
        private Long actorId;
        private String actorUsername;
        private Long postId;
    }
    
    @Data
    static class CommentNotifyRequest {
        private Long userId;
        private Long actorId;
        private String actorUsername;
        private Long postId;
        private Long commentId;
    }
}
