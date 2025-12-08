# Chat Service Fixes - Send Button Issue Resolution

## Issues Fixed

### 1. Entity Field Mismatch
**Problem**: Backend entity used `senderId`/`receiverId` (Long) but frontend expected `senderUsername`/`receiverUsername` (String)

**Solution**: Updated `ChatMessage.java` entity:
- Changed `senderId` → `senderUsername` (String)
- Changed `receiverId` → `receiverUsername` (String)
- Changed `isRead` → `read` (Boolean)
- Changed `createdAt` → `timestamp` (LocalDateTime)
- Added `messageType` field (String, default "TEXT")

### 2. Missing Backend Endpoints
**Problem**: Frontend called endpoints that didn't exist in backend

**Solution**: Added missing endpoints in `ChatController.java`:
- `POST /api/chat/mark-read/{username}` - Mark messages as read
- `GET /api/chat/contacts` - Get list of chat contacts
- `GET /api/chat/unread-count/{username}` - Get unread count for specific user
- `GET /api/chat/unread-counts` - Get unread counts for all contacts

### 3. Repository Query Updates
**Problem**: Repository queries used Long IDs instead of usernames

**Solution**: Updated `ChatMessageRepository.java`:
- Changed all queries to use username-based filtering
- Added `findUnreadMessages(String username)` method
- Added `findBySenderUsername(String username)` method
- Added `findByReceiverUsername(String username)` method
- Added `findAllByUsername(String username)` method

### 4. Service Layer Enhancements
**Problem**: Service layer missing business logic for new features

**Solution**: Updated `ChatService.java`:
- Changed method signatures to use String usernames instead of Long IDs
- Added `markAsRead(String currentUsername, String otherUsername)` method
- Added `getChatContacts(String username)` method
- Added `getUnreadCount(String currentUsername, String otherUsername)` method

### 5. API Gateway Authentication
**Problem**: Gateway only passed `X-User-Id` header, but chat service needs `X-Username`

**Solution**: Updated `AuthenticationFilter.java`:
- Extract username from JWT token claims
- Add `X-Username` header to forwarded requests
- Maintain backward compatibility with `X-User-Id` header

### 6. Frontend API URL
**Problem**: Frontend service used incorrect API path `/chat` instead of `/api/chat`

**Solution**: Updated `chat.service.ts`:
- Changed `apiUrl` from `/chat` to `/api/chat`

## Database Schema

### MongoDB Collection: `chat_messages`
```json
{
  "_id": "string",
  "senderUsername": "string",
  "receiverUsername": "string",
  "content": "string",
  "read": "boolean",
  "timestamp": "datetime",
  "messageType": "string"
}
```

## API Endpoints Summary

### Chat Service Endpoints (Port 8086)
All endpoints require JWT authentication via `Authorization: Bearer <token>` header

1. **Send Message**
   - `POST /api/chat/send`
   - Headers: `X-Username` (auto-added by gateway)
   - Body: `{ "receiverUsername": "string", "content": "string" }`
   - Response: `ChatMessage` object

2. **Get Conversation**
   - `GET /api/chat/conversation/{username}`
   - Headers: `X-Username` (auto-added by gateway)
   - Response: Array of `ChatMessage` objects

3. **Mark Messages as Read**
   - `POST /api/chat/mark-read/{username}`
   - Headers: `X-Username` (auto-added by gateway)
   - Response: Success message string

4. **Get Chat Contacts**
   - `GET /api/chat/contacts`
   - Headers: `X-Username` (auto-added by gateway)
   - Response: Array of username strings

5. **Get Unread Count for User**
   - `GET /api/chat/unread-count/{username}`
   - Headers: `X-Username` (auto-added by gateway)
   - Response: Number (unread count)

6. **Get All Unread Counts**
   - `GET /api/chat/unread-counts`
   - Headers: `X-Username` (auto-added by gateway)
   - Response: Array of `{ "username": "string", "unreadCount": number }`

## Testing Steps

1. **Start MongoDB**
   ```bash
   # Ensure MongoDB is running on localhost:27017
   ```

2. **Rebuild Chat Service**
   ```bash
   cd backend/chat-service
   mvn clean install
   ```

3. **Rebuild API Gateway**
   ```bash
   cd backend/api-gateway
   mvn clean install
   ```

4. **Restart Services**
   ```bash
   # Stop existing services
   # Start chat-service
   # Start api-gateway
   ```

5. **Test Frontend**
   - Login to the application
   - Navigate to chat module
   - Try sending a message
   - Verify message appears in conversation
   - Check unread counts update correctly

## Notes

- MongoDB is used for chat storage (not MySQL like other services)
- WebSocket support is configured but REST endpoints are primary
- All endpoints go through API Gateway (port 8080)
- JWT token must contain `username` claim for proper routing
- CORS is configured to allow requests from `http://localhost:4200`
