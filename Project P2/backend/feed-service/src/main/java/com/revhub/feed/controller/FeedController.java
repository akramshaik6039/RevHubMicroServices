package com.revhub.feed.controller;

import com.revhub.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {
    
    private final FeedService feedService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getFeed(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(feedService.getPersonalizedFeed(userId));
    }
}
