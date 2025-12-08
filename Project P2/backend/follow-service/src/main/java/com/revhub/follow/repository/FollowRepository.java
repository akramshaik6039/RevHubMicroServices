package com.revhub.follow.repository;

import com.revhub.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<Follow> findByFollowerId(Long followerId);
    List<Follow> findByFollowingId(Long followingId);
    
    @Query("SELECT f.followingId FROM Follow f WHERE f.followerId = :userId AND f.status = 'ACCEPTED'")
    List<Long> findFollowingIds(Long userId);
    
    @Query("SELECT f.followerId FROM Follow f WHERE f.followingId = :userId AND f.status = 'ACCEPTED'")
    List<Long> findFollowerIds(Long userId);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followingId = :userId AND f.status = 'ACCEPTED'")
    long countFollowers(Long userId);
    
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = :userId AND f.status = 'ACCEPTED'")
    long countFollowing(Long userId);
    
    @Query("SELECT f FROM Follow f WHERE f.followingId = :userId AND f.status = 'PENDING'")
    List<Follow> findPendingFollowRequests(Long userId);
    
    @Query("SELECT DISTINCT f.followerId FROM Follow f UNION SELECT DISTINCT f.followingId FROM Follow f")
    List<Long> findAllDistinctUserIds();
}
