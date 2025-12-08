package com.revhub.post.dto;

import com.revhub.post.entity.Comment;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private AuthorDTO author;
    private List<CommentResponse> replies;
    
    @Data
    public static class AuthorDTO {
        private Long id;
        private String username;
        private String profilePicture;
    }
    
    public static CommentResponse fromComment(Comment comment, AuthorDTO author) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedDate(comment.getCreatedDate());
        response.setAuthor(author);
        return response;
    }
}
