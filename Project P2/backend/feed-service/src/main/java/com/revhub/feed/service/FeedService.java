package com.revhub.feed.service;

import com.revhub.feed.client.FollowClient;
import com.revhub.feed.client.PostClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    
    private final PostClient postClient;
    private final FollowClient followClient;
    
    public List<Map<String, Object>> getPersonalizedFeed(Long userId) {
        try {
            List<Long> followingIds = followClient.getFollowing(userId);
            followingIds.add(userId);
            
            return followingIds.stream()
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
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
