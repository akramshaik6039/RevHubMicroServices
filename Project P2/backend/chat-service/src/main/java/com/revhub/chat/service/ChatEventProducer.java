package com.revhub.chat.service;

import com.revhub.chat.event.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatEventProducer {

    private static final String TOPIC = "chat-messages";
    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;

    public void sendChatMessageEvent(ChatMessageEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getMessageId(), event);
            log.info("Chat message event sent: {}", event.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send chat message event", e);
        }
    }
}
