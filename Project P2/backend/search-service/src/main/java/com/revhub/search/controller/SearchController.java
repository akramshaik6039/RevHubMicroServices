package com.revhub.search.controller;

import com.revhub.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchController {
    
    private final SearchService searchService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> search(@RequestParam String q, @RequestParam(defaultValue = "all") String type) {
        return ResponseEntity.ok(searchService.search(q, type));
    }
    
    @GetMapping("/posts")
    public ResponseEntity<List<Map<String, Object>>> searchPosts(@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchPosts(q));
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchUsers(q));
    }
    
    @GetMapping("/hashtags")
    public ResponseEntity<List<Map<String, Object>>> searchHashtags(@RequestParam String q) {
        return ResponseEntity.ok(searchService.searchHashtags(q));
    }
}
