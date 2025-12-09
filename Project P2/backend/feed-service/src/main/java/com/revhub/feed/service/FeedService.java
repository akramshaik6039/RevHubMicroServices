package com.revhub.feed.service;

import com.revhub.feed.client.FollowClient;
import com.revhub.feed.client.PostClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    
    private final PostClient postClient;
    private final FollowClient followClient;
    private final RedisTemplate<String, Object> redisTemplate;
    
    public List<Map<String, Object>> getPersonalizedFeed(Long userId) {
        String cacheKey = "feed:" + userId;
        
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return (List<Map<String, Object>>) cached;
            }
        } catch (Exception e) {
            // Redis unavailable, continue without cache
        }
        
        try {
            List<Long> followingIds = followClient.getFollowing(userId);
            followingIds.add(userId);
            
            List<Map<String, Object>> feed = followingIds.stream()
                    .flatMap(id -> {
                        try {
                            return postClient.getUserPosts(id).stream();
                        } catch (Exception e) {
                            return java.util.stream.Stream.empty();
                        }
                    })
                    .sorted((p1, p2) -> {
                        try {
                            String date1 = String.valueOf(p1.get("createdAt"));
                            String date2 = String.valueOf(p2.get("createdAt"));
                            return date2.compareTo(date1);
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .limit(50)
                    .collect(Collectors.toList());
            
            try {
                redisTemplate.opsForValue().set(cacheKey, feed, 5, TimeUnit.MINUTES);
            } catch (Exception e) {
                // Redis unavailable, continue without cache
            }
            
            return feed;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    @KafkaListener(topics = "post-events", groupId = "feed-service-group")
    public void handlePostEvent(String message) {
        try {
            redisTemplate.keys("feed:*").forEach(key -> redisTemplate.delete(key));
        } catch (Exception e) {
            // Redis unavailable
        }
    }
}
