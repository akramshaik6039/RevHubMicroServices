package com.revhub.post.service;

import com.revhub.post.client.UserClient;
import com.revhub.post.dto.CommentResponse;
import com.revhub.post.dto.PostResponse;
import com.revhub.post.entity.*;
import com.revhub.post.exception.PostNotFoundException;
import com.revhub.post.exception.UnauthorizedException;
import com.revhub.post.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ShareRepository shareRepository;
    private final HashtagRepository hashtagRepository;
    private final UserClient userClient;
    
    public Post createPost(Post post) {
        extractAndSaveHashtags(post.getContent());
        return postRepository.save(post);
    }
    
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
    }
    
    public List<Post> getUserPosts(Long userId) {
        return postRepository.findByUserIdOrderByCreatedDateDesc(userId);
    }
    
    public Page<Post> getUniversalPosts(Pageable pageable) {
        return postRepository.findByVisibilityOrderByCreatedDateDesc(PostVisibility.PUBLIC, pageable);
    }
    
    public Page<Post> getFollowersPosts(List<Long> userIds, Pageable pageable) {
        return postRepository.findByUserIdInAndVisibilityOrderByCreatedDateDesc(userIds, PostVisibility.PUBLIC, pageable);
    }
    
    public Post updatePost(Long id, Post updateData, Long userId) {
        Post post = getPostById(id);
        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }
        if (updateData.getContent() != null) {
            post.setContent(updateData.getContent());
            extractAndSaveHashtags(updateData.getContent());
        }
        if (updateData.getImageUrl() != null) post.setImageUrl(updateData.getImageUrl());
        if (updateData.getMediaType() != null) post.setMediaType(updateData.getMediaType());

        return postRepository.save(post);
    }
    
    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = getPostById(id);
        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }
        commentRepository.deleteByPostId(id);
        likeRepository.deleteByPostId(id);
        shareRepository.deleteByPostId(id);
        postRepository.deleteById(id);
    }
    
    @Transactional
    public Map<String, Object> toggleLike(Long postId, Long userId) {
        Post post = getPostById(postId);
        var existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        boolean isLiked;
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            isLiked = false;
        } else {
            Like like = new Like();
            like.setUserId(userId);
            like.setPostId(postId);
            likeRepository.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
            isLiked = true;
            
            if (!post.getUserId().equals(userId)) {
                try {
                    Map<String, Object> actor = userClient.getUserById(userId);
                    notifyLike(post.getUserId(), userId, (String) actor.get("username"), postId);
                } catch (Exception e) {
                    // Ignore notification errors
                }
            }
        }
        postRepository.save(post);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("likesCount", post.getLikesCount());
        response.put("isLiked", isLiked);
        return response;
    }
    
    private void notifyLike(Long userId, Long actorId, String actorUsername, Long postId) {
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            Map<String, Object> request = new java.util.HashMap<>();
            request.put("userId", userId);
            request.put("actorId", actorId);
            request.put("actorUsername", actorUsername);
            request.put("postId", postId);
            restTemplate.postForObject("http://localhost:8085/api/notifications/like", request, Void.class);
        } catch (Exception e) {
            // Ignore notification errors
        }
    }
    
    @Transactional
    public Map<String, Object> sharePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        Share share = new Share();
        share.setUserId(userId);
        share.setPostId(postId);
        shareRepository.save(share);
        post.setSharesCount(post.getSharesCount() + 1);
        postRepository.save(post);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("sharesCount", post.getSharesCount());
        return response;
    }
    
    public List<Post> searchPosts(String query) {
        return postRepository.searchByContent(query);
    }
    
    public PostResponse enrichPostWithAuthor(Post post) {
        PostResponse.AuthorDTO author = new PostResponse.AuthorDTO();
        try {
            Map<String, Object> user = userClient.getUserById(post.getUserId());
            author.setId(((Number) user.get("id")).longValue());
            author.setUsername((String) user.get("username"));
            author.setFullName((String) user.get("fullName"));
            author.setProfilePicture((String) user.get("profilePicture")); 
        } catch (Exception e) {
            author.setId(post.getUserId());
            author.setUsername("Unknown");
        }
        return PostResponse.fromPost(post, author);
    }
    
    public List<PostResponse> enrichPostsWithAuthors(List<Post> posts) {
        return posts.stream()
                .map(this::enrichPostWithAuthor)
                .collect(Collectors.toList());
    }
    
    public Page<PostResponse> enrichPostPageWithAuthors(Page<Post> posts) {
        return posts.map(this::enrichPostWithAuthor);
    }
    
    private void extractAndSaveHashtags(String content) {
        if (content == null) return;
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String tag = matcher.group(1).toLowerCase();
            var hashtag = hashtagRepository.findByName(tag);
            if (hashtag.isPresent()) {
                Hashtag h = hashtag.get();
                h.setCount(h.getCount() + 1);
                hashtagRepository.save(h);
            } else {
                Hashtag h = new Hashtag();
                h.setName(tag);
                h.setCount(1);
                hashtagRepository.save(h);
            }
        }
    }
    
    public List<Map<String, Object>> getHashtags(String query) {
        List<Hashtag> hashtags;
        if (query == null || query.trim().isEmpty()) {
            hashtags = hashtagRepository.findTop10ByOrderByCountDesc();
        } else {
            hashtags = hashtagRepository.findByNameContainingOrderByCountDesc(query.toLowerCase())
                .stream()
                .limit(10)
                .collect(Collectors.toList());
        }
        
        return hashtags.stream()
            .map(h -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("name", h.getName());
                map.put("count", h.getCount());
                return map;
            })
            .collect(Collectors.toList());
    }
}
