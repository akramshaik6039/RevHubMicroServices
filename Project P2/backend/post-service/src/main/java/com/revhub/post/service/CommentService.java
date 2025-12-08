package com.revhub.post.service;

import com.revhub.post.client.UserClient;
import com.revhub.post.dto.CommentResponse;
import com.revhub.post.entity.Comment;
import com.revhub.post.entity.Post;
import com.revhub.post.repository.CommentRepository;
import com.revhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserClient userClient;
    
    public List<Comment> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndParentCommentIdIsNullOrderByCreatedDateDesc(postId);
        comments.forEach(comment -> {
            List<Comment> replies = commentRepository.findByParentCommentIdOrderByCreatedDateAsc(comment.getId());
            comment.setReplies(replies);
        });
        return comments;
    }
    
    public List<CommentResponse> getCommentsWithAuthors(Long postId) {
        List<Comment> comments = getCommentsByPost(postId);
        return comments.stream()
                .map(this::enrichCommentWithAuthor)
                .collect(Collectors.toList());
    }
    
    private CommentResponse enrichCommentWithAuthor(Comment comment) {
        try {
            Map<String, Object> user = userClient.getUserById(comment.getUserId());
            CommentResponse.AuthorDTO author = new CommentResponse.AuthorDTO();
            author.setId(((Number) user.get("id")).longValue());
            author.setUsername((String) user.get("username"));
            author.setProfilePicture((String) user.get("profilePicture"));
            
            CommentResponse response = CommentResponse.fromComment(comment, author);
            
            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                List<CommentResponse> enrichedReplies = comment.getReplies().stream()
                        .map(this::enrichCommentWithAuthor)
                        .collect(Collectors.toList());
                response.setReplies(enrichedReplies);
            }
            
            return response;
        } catch (Exception e) {
            CommentResponse.AuthorDTO author = new CommentResponse.AuthorDTO();
            author.setId(comment.getUserId());
            author.setUsername("Unknown");
            return CommentResponse.fromComment(comment, author);
        }
    }
    
    @Transactional
    public Comment addComment(Long postId, String content, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment = commentRepository.save(comment);
        
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);
        
        if (!post.getUserId().equals(userId)) {
            try {
                Map<String, Object> actor = userClient.getUserById(userId);
                notifyComment(post.getUserId(), userId, (String) actor.get("username"), postId, comment.getId());
            } catch (Exception e) {
                // Ignore notification errors
            }
        }
        
        extractMentions(content, postId, comment.getId(), userId);
        
        return comment;
    }
    
    private void notifyComment(Long userId, Long actorId, String actorUsername, Long postId, Long commentId) {
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            Map<String, Object> request = new java.util.HashMap<>();
            request.put("userId", userId);
            request.put("actorId", actorId);
            request.put("actorUsername", actorUsername);
            request.put("postId", postId);
            request.put("commentId", commentId);
            restTemplate.postForObject("http://localhost:8085/api/notifications/comment", request, Void.class);
        } catch (Exception e) {
            // Ignore notification errors
        }
    }
    
    private void extractMentions(String content, Long postId, Long commentId, Long actorId) {
        if (content == null) return;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("@(\\w+)");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String username = matcher.group(1);
            try {
                Map<String, Object> actor = userClient.getUserById(actorId);
                Map<String, Object> mentionedUser = userClient.getUserByUsername(username);
                if (mentionedUser != null) {
                    Long mentionedUserId = ((Number) mentionedUser.get("id")).longValue();
                    notifyMention(mentionedUserId, actorId, (String) actor.get("username"), postId, commentId);
                }
            } catch (Exception e) {
                // Ignore if user not found
            }
        }
    }
    
    private void notifyMention(Long userId, Long actorId, String actorUsername, Long postId, Long commentId) {
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            Map<String, Object> request = new java.util.HashMap<>();
            request.put("userId", userId);
            request.put("actorId", actorId);
            request.put("actorUsername", actorUsername);
            request.put("postId", postId);
            request.put("commentId", commentId);
            restTemplate.postForObject("http://localhost:8085/api/notifications/mention", request, Void.class);
        } catch (Exception e) {
            // Ignore notification errors
        }
    }
    
    public Comment addReply(Long parentCommentId, String content, Long userId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        Comment reply = new Comment();
        reply.setPostId(parentComment.getPostId());
        reply.setUserId(userId);
        reply.setContent(content);
        reply.setParentCommentId(parentCommentId);
        
        return commentRepository.save(reply);
    }
    
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        commentRepository.delete(comment);
        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);
    }
}
