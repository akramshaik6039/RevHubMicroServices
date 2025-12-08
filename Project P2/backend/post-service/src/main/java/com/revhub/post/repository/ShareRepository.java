package com.revhub.post.repository;

import com.revhub.post.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Long> {
    void deleteByPostId(Long postId);
}
