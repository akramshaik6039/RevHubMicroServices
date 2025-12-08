package com.revhub.feed.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "post-service")
public interface PostClient {
    @GetMapping("/api/posts/user/{userId}")
    List<Map<String, Object>> getUserPosts(@PathVariable Long userId);
}
