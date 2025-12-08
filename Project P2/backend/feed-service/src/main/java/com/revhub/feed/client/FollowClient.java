package com.revhub.feed.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "follow-service")
public interface FollowClient {
    @GetMapping("/api/follows/{userId}/following")
    List<Long> getFollowing(@PathVariable Long userId);
}
