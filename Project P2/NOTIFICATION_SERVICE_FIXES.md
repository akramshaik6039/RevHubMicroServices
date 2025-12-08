# Notification Service Fixes - Complete Implementation

## Changes Made

### 1. **Notification Entity** - Updated fields to match frontend expectations
- `isRead` → `readStatus`
- `createdAt` → `createdDate`
- Added `actorUsername`, `actorProfilePicture`
- Added `postId`, `commentId`, `followRequestId` for context

### 2. **Notification Repository** - Updated query methods
- `findByUserIdOrderByCreatedAtDesc` → `findByUserIdOrderByCreatedDateDesc`
- `countByUserIdAndIsReadFalse` → `countByUserIdAndReadStatusFalse`
- Added `findByUserIdAndTypeOrderByCreatedDateDesc`

### 3. **Notification Service** - Added notification creation methods
- `createFollowNotification()` - When someone follows you
- `createFollowRequestNotification()` - When someone requests to follow (private account)
- `createLikeNotification()` - When someone likes your post
- `createCommentNotification()` - When someone comments on your post
- `createMentionNotification()` - When someone mentions you with @username
- `createMessageNotification()` - When someone sends you a message
- `deleteNotification()` - Delete a notification

### 4. **Notification Controller** - Added all missing endpoints
- `POST /api/notifications/follow` - Create follow notification
- `POST /api/notifications/follow-request` - Create follow request notification
- `POST /api/notifications/like` - Create like notification
- `POST /api/notifications/comment` - Create comment notification
- `POST /api/notifications/mention` - Create mention notification
- `POST /api/notifications/message` - Create message notification
- `POST /api/notifications/follow-request/{followId}/accept` - Accept follow request
- `POST /api/notifications/follow-request/{followId}/reject` - Reject follow request
- `DELETE /api/notifications/{id}` - Delete notification

### 5. **Post Service** - Added notification triggers
- Triggers like notification when post is liked (except self-likes)
- Gets actor username from user service

### 6. **Comment Service** - Added notification triggers
- Triggers comment notification when comment is added
- Extracts @mentions from comment content
- Triggers mention notification for each mentioned user
- Uses regex pattern `@(\w+)` to find mentions

### 7. **Follow Service** - Added notification triggers
- Triggers follow notification for public accounts
- Triggers follow request notification for private accounts
- Passes followRequestId for follow requests

### 8. **Chat Service** - Added notification trigger
- Triggers message notification when message is sent

### 9. **Frontend Notification Service** - Fixed API URL
- Changed from `/notifications` to `/api/notifications`

### 10. **User Client** - Added method for mentions
- Added `getUserByUsername()` method to fetch user by username for @mentions

## Notification Types

| Type | Trigger | Message | Context Fields |
|------|---------|---------|----------------|
| FOLLOW | User follows you | "{username} started following you" | actorId, actorUsername |
| FOLLOW_REQUEST | User requests to follow (private) | "{username} requested to follow you" | actorId, actorUsername, followRequestId |
| LIKE | User likes your post | "{username} liked your post" | actorId, actorUsername, postId |
| COMMENT | User comments on your post | "{username} commented on your post" | actorId, actorUsername, postId, commentId |
| MENTION | User mentions you in comment | "{username} mentioned you" | actorId, actorUsername, postId, commentId |
| MESSAGE | User sends you a message | "{username} sent you a message" | actorId, actorUsername |

## Service Communication Flow

### Like Notification Flow:
1. User likes post → Post Service
2. Post Service checks if not self-like
3. Post Service calls User Service to get actor username
4. Post Service calls Notification Service `/api/notifications/like`
5. Notification Service creates and saves notification

### Comment Notification Flow:
1. User adds comment → Post Service
2. Post Service checks if not own post
3. Post Service calls User Service to get actor username
4. Post Service calls Notification Service `/api/notifications/comment`
5. Post Service extracts @mentions from content
6. For each mention, calls User Service to get mentioned user
7. Post Service calls Notification Service `/api/notifications/mention`

### Follow Notification Flow:
1. User follows another → Follow Service
2. Follow Service checks if private account
3. If public: Follow Service calls Notification Service `/api/notifications/follow`
4. If private: Follow Service calls Notification Service `/api/notifications/follow-request`

### Message Notification Flow:
1. User sends message → Chat Service
2. Chat Service saves message
3. Chat Service calls Notification Service `/api/notifications/message`

## Database Schema

### MongoDB Collection: `notifications`
```json
{
  "_id": "string",
  "userId": "number",
  "actorId": "number",
  "actorUsername": "string",
  "actorProfilePicture": "string",
  "type": "string",
  "message": "string",
  "readStatus": "boolean",
  "createdDate": "datetime",
  "postId": "number",
  "commentId": "number",
  "followRequestId": "number"
}
```

## Service Ports
- User Service: 8081
- Post Service: 8082
- Follow Service: 8083
- Feed Service: 8084
- Notification Service: 8085
- Chat Service: 8086
- API Gateway: 8080

## Testing Steps

1. **Rebuild all modified services:**
   ```bash
   cd backend/notification-service && mvn clean install -DskipTests
   cd backend/post-service && mvn clean install -DskipTests
   cd backend/follow-service && mvn clean install -DskipTests
   cd backend/chat-service && mvn clean install -DskipTests
   ```

2. **Restart services in order:**
   - Start notification-service (8085)
   - Start post-service (8082)
   - Start follow-service (8083)
   - Start chat-service (8086)

3. **Test each notification type:**
   - Like a post → Check notifications
   - Comment on a post → Check notifications
   - Mention someone with @username → Check notifications
   - Follow someone → Check notifications
   - Send a message → Check notifications

## Notes

- All notification triggers are wrapped in try-catch to prevent failures
- Notifications are sent asynchronously (fire and forget)
- Self-actions (liking own post, etc.) don't trigger notifications
- MongoDB is used for notification storage
- All endpoints require JWT authentication via API Gateway
