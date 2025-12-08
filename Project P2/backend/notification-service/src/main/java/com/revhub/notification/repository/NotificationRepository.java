package com.revhub.notification.repository;

import com.revhub.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedDateDesc(Long userId);
    Long countByUserIdAndReadStatusFalse(Long userId);
    List<Notification> findByUserIdAndTypeOrderByCreatedDateDesc(Long userId, String type);
    List<Notification> findByFollowRequestId(Long followRequestId);
}
