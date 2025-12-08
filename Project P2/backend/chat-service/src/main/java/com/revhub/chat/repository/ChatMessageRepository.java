package com.revhub.chat.repository;

import com.revhub.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Query("{ $or: [ { 'senderUsername': ?0, 'receiverUsername': ?1 }, { 'senderUsername': ?1, 'receiverUsername': ?0 } ] }")
    List<ChatMessage> findConversation(String username1, String username2);
    
    @Query("{ 'receiverUsername': ?0, 'read': false }")
    List<ChatMessage> findUnreadMessages(String username);
    
    @Query("{ 'senderUsername': ?0 }")
    List<ChatMessage> findBySenderUsername(String username);
    
    @Query("{ 'receiverUsername': ?0 }")
    List<ChatMessage> findByReceiverUsername(String username);
    
    @Query("{ $or: [ { 'senderUsername': ?0 }, { 'receiverUsername': ?0 } ] }")
    List<ChatMessage> findAllByUsername(String username);
}
