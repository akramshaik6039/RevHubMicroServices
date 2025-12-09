# Kafka and Redis Implementation

## Summary

### Feed Service - Redis + Kafka ✅
- **Redis**: Implemented for caching personalized feeds (5-minute TTL)
- **Kafka**: Listens to `post-events` topic to invalidate cache when new posts are created

### Chat Service - WebSocket + Kafka + MongoDB ✅
- **WebSocket**: Already implemented for real-time messaging
- **Kafka**: Publishes to `chat-events` topic when messages are sent
- **MongoDB**: Already implemented for message persistence

## Implementation Details

### Feed Service

**Dependencies Added:**
- `spring-boot-starter-data-redis`
- `spring-kafka`

**Configuration (application.yml):**
```yaml
spring:
  data:
    redis:
      host: redis
      port: 6379
  kafka:
    bootstrap-servers: kafka:9092
    consumer:
      group-id: feed-service-group
      auto-offset-reset: earliest
```

**Features:**
1. **Redis Caching**: Feeds cached with key `feed:{userId}` for 5 minutes
2. **Kafka Consumer**: Listens to `post-events` and clears all feed caches
3. **Graceful Degradation**: Works without Redis/Kafka if unavailable

### Chat Service

**Dependencies Added:**
- `spring-kafka`

**Configuration (application.yml):**
```yaml
spring:
  kafka:
    bootstrap-servers: kafka:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

**Features:**
1. **Kafka Producer**: Publishes chat events to `chat-events` topic
2. **Event Format**: `message:{sender}:{receiver}`
3. **Graceful Degradation**: Works without Kafka if unavailable

## Docker Compose Updates

Added services:
- **Zookeeper**: Port 2181
- **Kafka**: Port 9092
- **Redis**: Port 6379 (already existed)

## Usage

### Start with Docker:
```bash
docker-compose up -d
```

### Kafka Topics:
- `post-events`: Post creation/update events
- `chat-events`: Chat message events

### Redis Keys:
- `feed:{userId}`: Cached personalized feed

## Testing

1. **Feed Caching**: First request slower, subsequent requests faster (from cache)
2. **Cache Invalidation**: Create a post, all feeds refresh automatically
3. **Chat Events**: Send message, event published to Kafka
