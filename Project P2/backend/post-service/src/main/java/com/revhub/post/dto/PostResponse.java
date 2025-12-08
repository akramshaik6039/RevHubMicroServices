package com.revhub.post.dto;

import com.revhub.post.entity.Post;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private String mediaType;
    private String visibility;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer sharesCount;
    private LocalDateTime createdAt;
    private AuthorDTO author;
    
    @Data
    public static class AuthorDTO {
        private Long id;
        private String username;
        private String fullName;
        private String profilePicture;
    }
    
    public static PostResponse fromPost(Post post, AuthorDTO author) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setImageUrl(post.getImageUrl());
        response.setMediaType(post.getMediaType());
        response.setVisibility(post.getVisibility().name());
        response.setLikesCount(post.getLikesCount());
        response.setCommentsCount(post.getCommentsCount());
        response.setSharesCount(post.getSharesCount());
        response.setCreatedAt(post.getCreatedDate());
        response.setAuthor(author);
        return response;
    }
}
