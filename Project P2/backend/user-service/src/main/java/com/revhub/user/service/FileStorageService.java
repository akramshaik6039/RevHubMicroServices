package com.revhub.user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final Path profilePicturesLocation = Paths.get("uploads/profiles");
    
    public FileStorageService() {
        try {
            Files.createDirectories(profilePicturesLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory");
        }
    }
    
    public String storeProfilePicture(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = profilePicturesLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation);
            return "/uploads/profiles/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file");
        }
    }
}
