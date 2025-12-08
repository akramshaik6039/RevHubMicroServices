package com.revhub.post.repository;

import com.revhub.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentCommentIdIsNullOrderByCreatedDateDesc(Long postId);
    List<Comment> findByParentCommentIdOrderByCreatedDateAsc(Long parentCommentId);
    void deleteByPostId(Long postId);
}
