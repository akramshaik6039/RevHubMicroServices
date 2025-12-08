package com.revhub.post.repository;

import com.revhub.post.entity.Post;
import com.revhub.post.entity.PostVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdOrderByCreatedDateDesc(Long userId);
    Page<Post> findByVisibilityOrderByCreatedDateDesc(PostVisibility visibility, Pageable pageable);
    Page<Post> findByUserIdInAndVisibilityOrderByCreatedDateDesc(List<Long> userIds, PostVisibility visibility, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchByContent(String query);
}
