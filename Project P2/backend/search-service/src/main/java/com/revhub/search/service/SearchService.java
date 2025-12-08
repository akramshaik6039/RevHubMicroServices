package com.revhub.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public Map<String, Object> search(String query, String type) {
        Map<String, Object> results = new HashMap<>();
        
        if (query.startsWith("@")) {
            results.put("users", searchUsers(query.substring(1)));
            results.put("posts", new ArrayList<>());
            results.put("hashtags", new ArrayList<>());
            return results;
        }
        
        if (query.startsWith("#")) {
            results.put("hashtags", searchHashtags(query.substring(1)));
            results.put("posts", searchPosts(query));
            results.put("users", new ArrayList<>());
            return results;
        }
        
        if ("all".equals(type) || "posts".equals(type)) {
            results.put("posts", searchPosts(query));
        }
        if ("all".equals(type) || "users".equals(type)) {
            results.put("users", searchUsers(query));
        }
        if ("all".equals(type) || "hashtags".equals(type)) {
            results.put("hashtags", searchHashtags(query));
        }
        
        return results;
    }
    
    public List<Map<String, Object>> searchPosts(String query) {
        try {
            String url = "http://localhost:8082/api/posts/search?q=" + query;
            List<Map<String, Object>> posts = restTemplate.getForObject(url, List.class);
            return posts != null ? posts : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Map<String, Object>> searchUsers(String query) {
        try {
            String url = "http://localhost:8081/api/users/search?q=" + query;
            List<Map<String, Object>> users = restTemplate.getForObject(url, List.class);
            return users != null ? users : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Map<String, Object>> searchHashtags(String query) {
        try {
            String cleanQuery = query.startsWith("#") ? query.substring(1) : query;
            String url = "http://localhost:8082/api/posts/hashtags?q=" + cleanQuery;
            List<Map<String, Object>> hashtags = restTemplate.getForObject(url, List.class);
            return hashtags != null ? hashtags : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
