package com.revhub.post.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final Path postMediaLocation = Paths.get("uploads/posts");
    
    public FileStorageService() {
        try {
            Files.createDirectories(postMediaLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory");
        }
    }
    
    public String storePostMedia(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = postMediaLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation);
            return "/uploads/posts/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file");
        }
    }
    
    public String getMediaType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) return "image";
            if (contentType.startsWith("video/")) return "video";
        }
        return "image";
    }
}
