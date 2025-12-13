package com.revhub.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> userFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User service is temporarily unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<Map<String, String>> postFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post service is temporarily unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<Map<String, String>> feedFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Feed service is temporarily unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}