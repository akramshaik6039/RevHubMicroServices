package com.revhub.user.controller;

import com.revhub.user.entity.User;
import com.revhub.user.service.FileStorageService;
import com.revhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final FileStorageService fileStorageService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateProfile(id, user));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }
    
    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<User> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String pictureUrl = fileStorageService.storeProfilePicture(file);
        return ResponseEntity.ok(userService.updateProfilePicture(id, pictureUrl));
    }
}
