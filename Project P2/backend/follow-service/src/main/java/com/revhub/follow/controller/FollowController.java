package com.revhub.follow.controller;

import com.revhub.follow.entity.Follow;
import com.revhub.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {
    
    private final FollowService followService;
    
    @PostMapping("/{followingId}")
    public ResponseEntity<Map<String, String>> followUser(
            @PathVariable Long followingId, 
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Boolean isPrivate) {
        try {
            String message = followService.followUser(userId, followingId, isPrivate);
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/{followingId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long followingId, @RequestHeader("X-User-Id") Long userId) {
        try {
            followService.unfollowUser(userId, followingId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Unfollowed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Long>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
    
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<Long>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }
    
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowersCount(userId));
    }
    
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowingCount(userId));
    }
    
    @GetMapping("/status/{followingId}")
    public ResponseEntity<Map<String, String>> getFollowStatus(
            @PathVariable Long followingId, 
            @RequestHeader("X-User-Id") Long userId) {
        try {
            String status = followService.getFollowStatus(userId, followingId);
            Map<String, String> response = new HashMap<>();
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/requests")
    public ResponseEntity<List<Follow>> getPendingFollowRequests(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<Follow> requests = followService.getPendingFollowRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/requests/{followId}/accept")
    public ResponseEntity<Map<String, String>> acceptFollowRequest(
            @PathVariable Long followId, 
            @RequestHeader("X-User-Id") Long userId) {
        try {
            followService.acceptFollowRequest(userId, followId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Follow request accepted");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/requests/{followId}/reject")
    public ResponseEntity<Map<String, String>> rejectFollowRequest(
            @PathVariable Long followId, 
            @RequestHeader("X-User-Id") Long userId) {
        try {
            followService.rejectFollowRequest(userId, followId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Follow request rejected");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/cancel/{followingId}")
    public ResponseEntity<Map<String, String>> cancelFollowRequest(
            @PathVariable Long followingId, 
            @RequestHeader("X-User-Id") Long userId) {
        try {
            followService.cancelFollowRequest(userId, followingId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Follow request cancelled");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/remove/{followerId}")
    public ResponseEntity<Map<String, String>> removeFollower(
            @PathVariable Long followerId, 
            @RequestHeader("X-User-Id") Long userId) {
        try {
            followService.removeFollower(userId, followerId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Follower removed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/suggestions")
    public ResponseEntity<List<Long>> getSuggestedUsers(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(followService.getSuggestedUsers(userId));
    }
}
