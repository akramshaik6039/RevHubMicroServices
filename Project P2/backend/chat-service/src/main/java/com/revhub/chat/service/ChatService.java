package com.revhub.chat.service;

import com.revhub.chat.entity.ChatMessage;
import com.revhub.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatMessageRepository chatMessageRepository;
    
    public ChatMessage sendMessage(ChatMessage message) {
        ChatMessage saved = chatMessageRepository.save(message);
        notifyMessage(message.getReceiverUsername(), message.getSenderUsername());
        return saved;
    }
    
    private void notifyMessage(String receiverUsername, String senderUsername) {
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            Map<String, Object> request = new java.util.HashMap<>();
            request.put("actorUsername", senderUsername);
            restTemplate.postForObject("http://localhost:8085/api/notifications/message", request, Void.class);
        } catch (Exception e) {
            // Ignore notification errors
        }
    }
    
    public List<ChatMessage> getConversation(String username1, String username2) {
        return chatMessageRepository.findConversation(username1, username2);
    }
    
    public void markAsRead(String currentUsername, String otherUsername) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findConversation(currentUsername, otherUsername)
            .stream()
            .filter(msg -> msg.getReceiverUsername().equals(currentUsername) && !msg.getRead())
            .collect(Collectors.toList());
        
        unreadMessages.forEach(msg -> msg.setRead(true));
        chatMessageRepository.saveAll(unreadMessages);
    }
    
    public List<String> getChatContacts(String username) {
        List<ChatMessage> messages = chatMessageRepository.findAllByUsername(username);
        Set<String> contacts = messages.stream()
            .map(msg -> msg.getSenderUsername().equals(username) ? msg.getReceiverUsername() : msg.getSenderUsername())
            .collect(Collectors.toSet());
        return contacts.stream().sorted().collect(Collectors.toList());
    }
    
    public long getUnreadCount(String currentUsername, String otherUsername) {
        return chatMessageRepository.findConversation(currentUsername, otherUsername)
            .stream()
            .filter(msg -> msg.getReceiverUsername().equals(currentUsername) && !msg.getRead())
            .count();
    }
}
