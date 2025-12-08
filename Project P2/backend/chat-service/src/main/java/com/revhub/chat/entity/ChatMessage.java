package com.revhub.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Data
public class ChatMessage {
    @Id
    private String id;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private Boolean read = false;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String messageType = "TEXT";
}
