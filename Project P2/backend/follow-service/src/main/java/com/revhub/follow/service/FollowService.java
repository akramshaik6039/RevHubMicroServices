package com.revhub.follow.service;

import com.revhub.follow.entity.Follow;
import com.revhub.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    
    private final FollowRepository followRepository;
    private final RestTemplate restTemplate;
    
    public String followUser(Long followerId, Long followingId, Boolean isPrivate) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Cannot follow yourself");
        }
        
        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        
        if (existingFollow.isPresent()) {
            Follow follow = existingFollow.get();
            if (follow.getStatus() == Follow.FollowStatus.ACCEPTED) {
                throw new RuntimeException("Already following this user");
            } else {
                throw new RuntimeException("Follow request already sent");
            }
        }
        
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        
        if (isPrivate != null && isPrivate) {
            follow.setStatus(Follow.FollowStatus.PENDING);
            Follow saved = followRepository.save(follow);
            notifyFollowRequest(followingId, followerId, saved.getId());
            return "Follow request sent";
        } else {
            follow.setStatus(Follow.FollowStatus.ACCEPTED);
            followRepository.save(follow);
            notifyFollow(followingId, followerId);
            return "Now following user";
        }
    }
    
    private void notifyFollow(Long userId, Long actorId) {
        try {
            java.util.Map<String, Object> actor = restTemplate.getForObject("http://localhost:8081/api/users/" + actorId, java.util.Map.class);
            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("userId", userId);
            request.put("actorId", actorId);
            request.put("actorUsername", actor.get("username"));
            restTemplate.postForObject("http://localhost:8085/api/notifications/follow", request, Void.class);
        } catch (Exception e) {
            // Ignore notification errors
        }
    }
    
    private void notifyFollowRequest(Long userId, Long actorId, Long followRequestId) {
        try {
            java.util.Map<String, Object> actor = restTemplate.getForObject("http://localhost:8081/api/users/" + actorId, java.util.Map.class);
            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("userId", userId);
            request.put("actorId", actorId);
            request.put("actorUsername", actor.get("username"));
            request.put("followRequestId", followRequestId);
            restTemplate.postForObject("http://localhost:8085/api/notifications/follow-request", request, Void.class);
        } catch (Exception e) {
            // Ignore notification errors
        }
    }
    
    public void unfollowUser(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new RuntimeException("Not following this user"));
        followRepository.delete(follow);
    }
    
    public List<Long> getFollowing(Long userId) {
        return followRepository.findFollowingIds(userId);
    }
    
    public List<Long> getFollowers(Long userId) {
        return followRepository.findFollowerIds(userId);
    }
    
    public long getFollowersCount(Long userId) {
        return followRepository.countFollowers(userId);
    }
    
    public long getFollowingCount(Long userId) {
        return followRepository.countFollowing(userId);
    }
    
    public String getFollowStatus(Long followerId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        
        if (follow.isPresent()) {
            return follow.get().getStatus().toString();
        }
        
        return "NOT_FOLLOWING";
    }
    
    public List<Follow> getPendingFollowRequests(Long userId) {
        return followRepository.findPendingFollowRequests(userId);
    }
    
    public void acceptFollowRequest(Long userId, Long followId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new RuntimeException("Follow request not found"));
        
        if (!follow.getFollowingId().equals(userId)) {
            throw new RuntimeException("Unauthorized to accept this request");
        }
        
        if (follow.getStatus() != Follow.FollowStatus.PENDING) {
            throw new RuntimeException("Follow request is not pending");
        }
        
        follow.setStatus(Follow.FollowStatus.ACCEPTED);
        followRepository.save(follow);
        
        removeFollowRequestNotification(followId);
        notifyFollow(follow.getFollowerId(), follow.getFollowingId());
    }
    
    private void removeFollowRequestNotification(Long followRequestId) {
        try {
            restTemplate.delete("http://localhost:8085/api/notifications/follow-request/" + followRequestId);
        } catch (Exception e) {
            // Ignore
        }
    }
    
    public void rejectFollowRequest(Long userId, Long followId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new RuntimeException("Follow request not found"));
        
        if (!follow.getFollowingId().equals(userId)) {
            throw new RuntimeException("Unauthorized to reject this request");
        }
        
        if (follow.getStatus() != Follow.FollowStatus.PENDING) {
            throw new RuntimeException("Follow request is not pending");
        }
        
        followRepository.delete(follow);
        removeFollowRequestNotification(followId);
    }
    
    public void cancelFollowRequest(Long followerId, Long followingId) {
        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        
        if (follow.isPresent() && follow.get().getStatus() == Follow.FollowStatus.PENDING) {
            followRepository.delete(follow.get());
        } else {
            throw new RuntimeException("No pending follow request found");
        }
    }
    
    public void removeFollower(Long userId, Long followerId) {
        Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, userId);
        
        if (follow.isPresent()) {
            followRepository.delete(follow.get());
        } else {
            throw new RuntimeException("This user is not following you");
        }
    }
    
    public List<Long> getSuggestedUsers(Long userId) {
        List<Long> following = followRepository.findFollowingIds(userId);
        List<Long> allUserIds = followRepository.findAllDistinctUserIds();
        
        return allUserIds.stream()
                .filter(id -> !id.equals(userId) && !following.contains(id))
                .limit(5)
                .toList();
    }
}
