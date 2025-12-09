package com.revhub.post.controller;

import com.revhub.post.dto.CommentResponse;
import com.revhub.post.dto.PostResponse;
import com.revhub.post.entity.Comment;
import com.revhub.post.entity.Post;
import com.revhub.post.service.CommentService;
import com.revhub.post.service.FileStorageService;
import com.revhub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "universal") String feedType,
            @RequestParam(required = false) List<Long> followingIds) {
        
        Page<Post> posts;
        if ("followers".equals(feedType) && followingIds != null && !followingIds.isEmpty()) {
            posts = postService.getFollowersPosts(followingIds, PageRequest.of(page, size));
        } else {
            posts = postService.getUniversalPosts(PageRequest.of(page, size));
        }
        
        Page<PostResponse> enrichedPosts = postService.enrichPostPageWithAuthors(posts);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", enrichedPosts.getContent());
        response.put("totalElements", enrichedPosts.getTotalElements());
        response.put("totalPages", enrichedPosts.getTotalPages());
        response.put("size", enrichedPosts.getSize());
        response.put("number", enrichedPosts.getNumber());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestHeader("X-User-Id") Long userId) {
        System.out.println("Creating post for userId: " + userId + ", content: " + post.getContent());
        post.setUserId(userId);
        if (post.getVisibility() == null) {
            post.setVisibility(com.revhub.post.entity.PostVisibility.PUBLIC);
        }
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            if (post.getImageUrl().startsWith("data:video/")) {
                post.setMediaType("video");
            } else if (post.getImageUrl().startsWith("data:image/")) {
                post.setMediaType("image");
            }
        }
        Post createdPost = postService.createPost(post);
        System.out.println("Post created with ID: " + createdPost.getId() + " for userId: " + createdPost.getUserId());
        return ResponseEntity.ok(createdPost);
    }
    
    @PostMapping("/upload")
    public ResponseEntity<Post> createPostWithMedia(
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "visibility", defaultValue = "PUBLIC") String visibility,
            @RequestHeader("X-User-Id") Long userId) {
        
        System.out.println("Creating post with media for userId: " + userId + ", content: " + content);
        
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        post.setVisibility(com.revhub.post.entity.PostVisibility.valueOf(visibility));
        
        if (file != null && !file.isEmpty()) {
            try {
                byte[] fileBytes = file.getBytes();
                String base64File = java.util.Base64.getEncoder().encodeToString(fileBytes);
                String mimeType = file.getContentType();
                String dataUrl = "data:" + mimeType + ";base64," + base64File;
                post.setImageUrl(dataUrl);
                
                if (mimeType != null) {
                    if (mimeType.startsWith("image/")) {
                        post.setMediaType("image");
                    } else if (mimeType.startsWith("video/")) {
                        post.setMediaType("video");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error processing file");
            }
        }
        
        Post createdPost = postService.createPost(post);
        System.out.println("Post with media created with ID: " + createdPost.getId() + " for userId: " + createdPost.getUserId());
        return ResponseEntity.ok(createdPost);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable Long userId) {
        System.out.println("Getting posts for userId: " + userId);
        List<Post> posts = postService.getUserPosts(userId);
        System.out.println("Found " + posts.size() + " posts for userId: " + userId);
        return ResponseEntity.ok(postService.enrichPostsWithAuthors(posts));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(postService.updatePost(id, post, userId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(postService.toggleLike(id, userId));
    }
    
    @PostMapping("/{id}/toggle-like")
    public ResponseEntity<Map<String, Object>> toggleLikeAlt(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(postService.toggleLike(id, userId));
    }
    
    @PostMapping("/{id}/share")
    public ResponseEntity<Map<String, Object>> sharePost(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(postService.sharePost(id, userId));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String q) {
        return ResponseEntity.ok(postService.searchPosts(q));
    }
    
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsWithAuthors(id));
    }
    
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long id, @RequestBody Map<String, String> request, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(commentService.addComment(id, request.get("content"), userId));
    }
    
    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<Comment> addReply(@PathVariable Long commentId, @RequestBody Map<String, String> request, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(commentService.addReply(commentId, request.get("content"), userId));
    }
    
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestHeader("X-User-Id") Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/hashtags")
    public ResponseEntity<List<Map<String, Object>>> getHashtags(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(postService.getHashtags(q));
    }
}
