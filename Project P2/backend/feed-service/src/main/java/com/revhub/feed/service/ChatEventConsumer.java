package com.revhub.feed.service;

import com.revhub.feed.event.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatEventConsumer {

    @KafkaListener(topics = "chat-messages", groupId = "feed-service-group")
    public void consumeChatMessage(ChatMessageEvent event) {
        try {
            log.info("Received chat message event: {} from {} to {}", 
                event.getMessageId(), event.getSenderUsername(), event.getReceiverUsername());
            
            // Process chat event for feed
            // You can store this in Redis or use it to update user feeds
            // For now, just logging the event
            
        } catch (Exception e) {
            log.error("Error processing chat message event", e);
        }
    }
}
