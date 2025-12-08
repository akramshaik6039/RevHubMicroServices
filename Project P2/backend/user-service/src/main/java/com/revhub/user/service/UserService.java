package com.revhub.user.service;

import com.revhub.user.entity.User;
import com.revhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User updateProfile(Long userId, User updateData) {
        User user = getUserById(userId);
        if (updateData.getFullName() != null) user.setFullName(updateData.getFullName());
        if (updateData.getBio() != null) user.setBio(updateData.getBio());
        if (updateData.getProfilePicture() != null) user.setProfilePicture(updateData.getProfilePicture());
        if (updateData.getCoverPicture() != null) user.setCoverPicture(updateData.getCoverPicture());
        return userRepository.save(user);
    }
    
    public User updateProfilePicture(Long userId, String pictureUrl) {
        User user = getUserById(userId);
        user.setProfilePicture(pictureUrl);
        return userRepository.save(user);
    }
    
    public List<User> searchUsers(String query) {
        String lowerQuery = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) || 
                            (u.getFullName() != null && u.getFullName().toLowerCase().contains(lowerQuery)))
                .limit(20)
                .toList();
    }
}
