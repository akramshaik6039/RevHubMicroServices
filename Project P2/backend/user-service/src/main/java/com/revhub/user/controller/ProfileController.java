package com.revhub.user.controller;

import com.revhub.user.entity.User;
import com.revhub.user.repository.UserRepository;
import com.revhub.user.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final RestTemplate restTemplate;
    
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    Map<String, Object> profile = new HashMap<>();
                    profile.put("id", user.getId());
                    profile.put("username", user.getUsername());
                    profile.put("email", user.getEmail());
                    profile.put("profilePicture", user.getProfilePicture());
                    profile.put("bio", user.getBio());
                    profile.put("isPrivate", user.getIsPrivate());
                    profile.put("createdAt", user.getCreatedAt());
                    
                    try {
                        Long followersCount = restTemplate.getForObject(
                            "http://follow-service:8083/api/follows/" + user.getId() + "/followers/count", Long.class);
                        Long followingCount = restTemplate.getForObject(
                            "http://follow-service:8083/api/follows/" + user.getId() + "/following/count", Long.class);
                        profile.put("followersCount", followersCount != null ? followersCount : 0);
                        profile.put("followingCount", followingCount != null ? followingCount : 0);
                    } catch (Exception e) {
                        profile.put("followersCount", 0);
                        profile.put("followingCount", 0);
                    }
                    
                    return ResponseEntity.ok(profile);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updates, @RequestHeader("X-User-Id") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (updates.containsKey("bio")) {
                user.setBio(updates.get("bio"));
            }
            if (updates.containsKey("profilePicture")) {
                user.setProfilePicture(updates.get("profilePicture"));
            }
            if (updates.containsKey("isPrivate")) {
                user.setIsPrivate(Boolean.parseBoolean(updates.get("isPrivate")));
            }
            
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        try {
            List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    @GetMapping("/{username}/posts")
    public ResponseEntity<List> getUserPosts(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ResponseEntity<List> response = restTemplate.getForEntity(
                "http://post-service:8082/api/posts/user/" + user.getId(),
                List.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    @PostMapping("/follow/{username}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable String username, @RequestHeader("X-User-Id") Long userId) {
        try {
            System.out.println("Follow request for username: " + username + " by userId: " + userId);
            User targetUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            System.out.println("Target user found: " + targetUser.getId() + ", isPrivate: " + targetUser.getIsPrivate());
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            String url = "http://follow-service:8083/api/follows/" + targetUser.getId() + "?isPrivate=" + (targetUser.getIsPrivate() != null ? targetUser.getIsPrivate() : false);
            System.out.println("Calling follow-service: " + url);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            System.out.println("Follow-service response: " + response.getBody());
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            System.err.println("Error in followUser: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/unfollow/{username}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable String username, @RequestHeader("X-User-Id") Long userId) {
        try {
            User targetUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/" + targetUser.getId(),
                HttpMethod.DELETE,
                entity,
                Map.class
            );
            
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/follow-status/{username}")
    public ResponseEntity<Map<String, String>> getFollowStatus(@PathVariable String username, @RequestHeader("X-User-Id") Long userId) {
        try {
            User targetUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/status/" + targetUser.getId(),
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/follow-requests")
    public ResponseEntity<List<Map<String, Object>>> getPendingFollowRequests(@RequestHeader("X-User-Id") Long userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/requests",
                HttpMethod.GET,
                entity,
                List.class
            );
            
            List<Map<String, Object>> follows = (List<Map<String, Object>>) response.getBody();
            List<Map<String, Object>> enrichedFollows = new ArrayList<>();
            
            for (Map<String, Object> follow : follows) {
                Long followerId = ((Number) follow.get("followerId")).longValue();
                User follower = userRepository.findById(followerId).orElse(null);
                
                Map<String, Object> enriched = new HashMap<>(follow);
                enriched.put("follower", follower);
                enrichedFollows.add(enriched);
            }
            
            return ResponseEntity.ok(enrichedFollows);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/follow-requests/{followId}/accept")
    public ResponseEntity<Map<String, String>> acceptFollowRequest(@PathVariable Long followId, @RequestHeader("X-User-Id") Long userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/requests/" + followId + "/accept",
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/follow-requests/{followId}/reject")
    public ResponseEntity<Map<String, String>> rejectFollowRequest(@PathVariable Long followId, @RequestHeader("X-User-Id") Long userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/requests/" + followId + "/reject",
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/cancel-request/{username}")
    public ResponseEntity<Map<String, String>> cancelFollowRequest(@PathVariable String username, @RequestHeader("X-User-Id") Long userId) {
        try {
            User targetUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/cancel/" + targetUser.getId(),
                HttpMethod.DELETE,
                entity,
                Map.class
            );
            
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ResponseEntity<List> response = restTemplate.getForEntity(
                "http://follow-service:8083/api/follows/" + user.getId() + "/followers",
                List.class
            );
            
            List<User> followers = new ArrayList<>();
            List<?> followerIds = response.getBody();
            
            if (followerIds != null && !followerIds.isEmpty()) {
                for (Object idObj : followerIds) {
                    Long id = ((Number) idObj).longValue();
                    userRepository.findById(id).ifPresent(followers::add);
                }
            }
            
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    @GetMapping("/{username}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ResponseEntity<List> response = restTemplate.getForEntity(
                "http://follow-service:8083/api/follows/" + user.getId() + "/following",
                List.class
            );
            
            List<User> following = new ArrayList<>();
            List<?> followingIds = response.getBody();
            
            if (followingIds != null && !followingIds.isEmpty()) {
                for (Object idObj : followingIds) {
                    Long id = ((Number) idObj).longValue();
                    userRepository.findById(id).ifPresent(following::add);
                }
            }
            
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    @DeleteMapping("/remove-follower/{username}")
    public ResponseEntity<Map<String, String>> removeFollower(@PathVariable String username, @RequestHeader("X-User-Id") Long userId) {
        try {
            User followerUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId.toString());
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://follow-service:8083/api/follows/remove/" + followerUser.getId(),
                HttpMethod.DELETE,
                entity,
                Map.class
            );
            
            return ResponseEntity.ok((Map<String, String>) response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadProfilePhoto(@RequestParam("file") MultipartFile file, @RequestHeader("X-User-Id") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            String profilePictureUrl = fileStorageService.storeProfilePicture(file);
            user.setProfilePicture(profilePictureUrl);
            userRepository.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("profilePictureUrl", profilePictureUrl);
            response.put("message", "Profile photo updated successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload profile photo: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    @GetMapping("/suggestions")
    public ResponseEntity<List<User>> getSuggestedUsers(@RequestHeader("X-User-Id") Long userId) {
        try {
            // Get user's following list
            List<Long> following = new ArrayList<>();
            try {
                ResponseEntity<List> response = restTemplate.getForEntity(
                    "http://follow-service:8083/api/follows/" + userId + "/following",
                    List.class
                );
                List<?> followingIds = response.getBody();
                if (followingIds != null) {
                    for (Object idObj : followingIds) {
                        following.add(((Number) idObj).longValue());
                    }
                }
            } catch (Exception e) {
                // Ignore, continue with empty following list
            }
            
            // Get all users except current user and already following
            List<User> allUsers = userRepository.findAll();
            List<User> suggestions = new ArrayList<>();
            
            for (User user : allUsers) {
                if (!user.getId().equals(userId) && !following.contains(user.getId())) {
                    suggestions.add(user);
                    if (suggestions.size() >= 5) break; // Limit to 5 suggestions
                }
            }
            
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}
