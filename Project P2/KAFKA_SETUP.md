# Kafka Integration - Chat & Feed Services

## Overview
Kafka has been integrated into chat-service (producer) and feed-service (consumer) for real-time event streaming.

## Architecture
- **Chat Service**: Publishes `ChatMessageEvent` to Kafka topic `chat-messages`
- **Feed Service**: Consumes `ChatMessageEvent` from Kafka topic `chat-messages`

## What Was Added

### 1. Docker Compose
- Zookeeper (port 2181)
- Kafka broker (port 9092)

### 2. Chat Service (Producer)
- **Dependencies**: spring-kafka
- **Event Model**: `ChatMessageEvent.java`
- **Producer Config**: `KafkaProducerConfig.java`
- **Producer Service**: `ChatEventProducer.java`
- **Updated**: `ChatService.java` to publish events

### 3. Feed Service (Consumer)
- **Dependencies**: spring-kafka
- **Event Model**: `ChatMessageEvent.java`
- **Consumer Config**: `KafkaConsumerConfig.java`
- **Consumer Service**: `ChatEventConsumer.java`

## How to Run

### Start Services
```bash
# Start all services including Kafka
docker-compose up -d

# Or start locally
start-all.bat
```

### Verify Kafka is Running
```bash
# Check Kafka container
docker ps | findstr kafka

# Check Kafka logs
docker logs revhub-kafka
```

### Test the Integration
1. Send a chat message via chat-service API
2. Check feed-service logs to see the consumed event

## Configuration

### Chat Service (application.yml)
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### Feed Service (application.yml)
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: feed-service-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
```

## Kafka Topics
- `chat-messages`: Chat message events

## Event Flow
1. User sends message → Chat Service
2. Chat Service saves to MongoDB
3. Chat Service publishes `ChatMessageEvent` to Kafka
4. Feed Service consumes event
5. Feed Service processes for user feeds

## Troubleshooting

### Kafka not connecting
- Ensure Zookeeper is running first
- Check `KAFKA_ADVERTISED_LISTENERS` in docker-compose.yml
- Verify bootstrap-servers configuration

### Messages not being consumed
- Check consumer group ID
- Verify topic name matches
- Check feed-service logs for errors

## Next Steps
You can extend this by:
- Adding more event types (MessageRead, MessageDeleted)
- Storing chat activity in Redis for feed display
- Adding Kafka monitoring tools
- Implementing dead letter queues for failed messages
