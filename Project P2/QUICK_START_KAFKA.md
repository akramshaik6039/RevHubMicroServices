# Quick Start Guide - RevHub with Kafka

## ✅ What's Ready
- Chat Service: Built with Kafka producer
- Feed Service: Built with Kafka consumer
- Docker Compose: Updated with Kafka & Zookeeper
- Startup Scripts: Created

## 🚀 How to Start

### Option 1: Using the Script (Recommended)
```bash
start-with-kafka.bat
```

### Option 2: Manual Start

**Step 1: Start Infrastructure**
```bash
docker-compose up -d mysql mongodb redis consul zookeeper kafka
```

**Step 2: Wait 30 seconds for Kafka to be ready**

**Step 3: Start Backend Services**
```bash
# In separate terminals:
cd backend\config-server && mvn spring-boot:run
cd backend\api-gateway && mvn spring-boot:run
cd backend\user-service && mvn spring-boot:run
cd backend\post-service && mvn spring-boot:run
cd backend\feed-service && mvn spring-boot:run
cd backend\follow-service && mvn spring-boot:run
cd backend\notification-service && mvn spring-boot:run
cd backend\chat-service && mvn spring-boot:run
cd backend\search-service && mvn spring-boot:run
```

**Step 4: Start Frontend**
```bash
cd frontend && npm start
```

## 🧪 Test Kafka Integration

### 1. Check Kafka is Running
```bash
test-kafka.bat
```

### 2. Send a Chat Message
Use the frontend or API:
```bash
POST http://localhost:8080/api/chat/send
{
  "senderUsername": "user1",
  "receiverUsername": "user2",
  "content": "Hello!"
}
```

### 3. Check Feed Service Logs
Look for: `Received chat message event: ...`

## 📊 Service Ports
- Frontend: http://localhost:4200
- API Gateway: http://localhost:8080
- User Service: http://localhost:8081
- Post Service: http://localhost:8082
- Follow Service: http://localhost:8083
- Feed Service: http://localhost:8084 (Kafka Consumer)
- Notification Service: http://localhost:8085
- Chat Service: http://localhost:8086 (Kafka Producer)
- Search Service: http://localhost:8087
- Config Server: http://localhost:8888
- Consul UI: http://localhost:8500
- Kafka: localhost:9092
- Zookeeper: localhost:2181

## 🔍 Verify Kafka Integration

### Check Chat Service Logs
Should see: `Chat message event sent: <messageId>`

### Check Feed Service Logs
Should see: `Received chat message event: <messageId> from user1 to user2`

## 🛠️ Troubleshooting

### Kafka not starting
```bash
docker logs revhub-kafka
docker logs revhub-zookeeper
```

### Services can't connect to Kafka
- Ensure Kafka is fully started (wait 30 seconds)
- Check docker-compose logs: `docker-compose logs kafka`

### No events in Feed Service
- Check Chat Service logs for "Chat message event sent"
- Verify Kafka topic exists: 
  ```bash
  docker exec -it revhub-kafka kafka-topics --list --bootstrap-server localhost:9092
  ```

## 📝 What Happens When You Send a Message

1. User sends chat message → Chat Service API
2. Chat Service saves to MongoDB
3. Chat Service publishes `ChatMessageEvent` to Kafka topic `chat-messages`
4. Feed Service consumes event from Kafka
5. Feed Service logs the event (you can extend this to update feeds)

## 🎯 Next Steps
- Extend Feed Service to store chat activity in Redis
- Add more event types (MessageRead, MessageDeleted)
- Implement real-time feed updates
- Add Kafka monitoring dashboard
