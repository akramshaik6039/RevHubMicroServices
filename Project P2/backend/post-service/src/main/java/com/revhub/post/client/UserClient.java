package com.revhub.post.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    Map<String, Object> getUserById(@PathVariable Long id);
    
    @GetMapping("/api/users/username/{username}")
    Map<String, Object> getUserByUsername(@PathVariable String username);
}
