package com.revhub.chat.controller;

import com.revhub.chat.entity.ChatMessage;
import com.revhub.chat.service.ChatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody SendMessageRequest request, @RequestHeader("X-Username") String username) {
        ChatMessage message = new ChatMessage();
        message.setSenderUsername(username);
        message.setReceiverUsername(request.getReceiverUsername());
        message.setContent(request.getContent());
        return ResponseEntity.ok(chatService.sendMessage(message));
    }
    
    @GetMapping("/conversation/{username}")
    public ResponseEntity<List<ChatMessage>> getConversation(@PathVariable String username, @RequestHeader("X-Username") String currentUsername) {
        return ResponseEntity.ok(chatService.getConversation(currentUsername, username));
    }
    
    @PostMapping("/mark-read/{username}")
    public ResponseEntity<String> markAsRead(@PathVariable String username, @RequestHeader("X-Username") String currentUsername) {
        chatService.markAsRead(currentUsername, username);
        return ResponseEntity.ok("Messages marked as read");
    }
    
    @GetMapping("/contacts")
    public ResponseEntity<List<String>> getChatContacts(@RequestHeader("X-Username") String username) {
        return ResponseEntity.ok(chatService.getChatContacts(username));
    }
    
    @GetMapping("/unread-count/{username}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable String username, @RequestHeader("X-Username") String currentUsername) {
        return ResponseEntity.ok(chatService.getUnreadCount(currentUsername, username));
    }
    
    @GetMapping("/unread-counts")
    public ResponseEntity<List<Map<String, Object>>> getAllUnreadCounts(@RequestHeader("X-Username") String currentUsername) {
        List<String> contacts = chatService.getChatContacts(currentUsername);
        List<Map<String, Object>> unreadCounts = contacts.stream()
            .map(contact -> {
                Map<String, Object> map = new HashMap<>();
                map.put("username", contact);
                map.put("unreadCount", chatService.getUnreadCount(currentUsername, contact));
                return map;
            })
            .toList();
        return ResponseEntity.ok(unreadCounts);
    }
    
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        return chatService.sendMessage(message);
    }
    
    @Data
    static class SendMessageRequest {
        private String receiverUsername;
        private String content;
    }
}
