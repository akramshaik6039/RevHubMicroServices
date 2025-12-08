# Search Implementation - Complete Guide

## Features Implemented

### 1. **User Search** (@username)
- Search users by username or full name
- Case-insensitive search
- Limited to top 20 results
- Prefix with @ for direct user search

### 2. **Post Search** (keyword)
- Search posts by content
- Search by author username
- Search by hashtags in content
- Returns posts with author information

### 3. **Hashtag Search** (#hashtag)
- Search hashtags with suggestions
- Ordered by usage count (most popular first)
- Top 10 suggestions
- Prefix with # for direct hashtag search

### 4. **Smart Search**
- Detects @ prefix → Returns only users
- Detects # prefix → Returns hashtags and related posts
- No prefix → Returns all (users, posts, hashtags)

## Backend Implementation

### Search Service (Port 8087)
**Endpoints:**
- `GET /api/search?q={query}&type={all|users|posts|hashtags}` - Universal search
- `GET /api/search/users?q={query}` - User search only
- `GET /api/search/posts?q={query}` - Post search only
- `GET /api/search/hashtags?q={query}` - Hashtag suggestions

**Logic:**
- Aggregates results from user-service and post-service
- Smart prefix detection (@, #)
- Returns combined results

### User Service (Port 8081)
**Endpoint:**
- `GET /api/users/search?q={query}` - Search users

**Implementation:**
- Searches username and fullName fields
- Case-insensitive
- Limit 20 results

### Post Service (Port 8082)
**Endpoints:**
- `GET /api/posts/search?q={query}` - Search posts
- `GET /api/posts/hashtags?q={query}` - Get hashtag suggestions

**Hashtag Repository Methods:**
```java
findByNameContainingOrderByCountDesc(String query) // Search with count ordering
findTop10ByOrderByCountDesc() // Top 10 popular hashtags
```

**Implementation:**
- Searches post content
- Searches author username
- Detects hashtags in content
- Returns enriched posts with author info
- Hashtag suggestions ordered by usage count

## Database Schema

### Hashtags Table (MySQL - post-service)
```sql
CREATE TABLE hashtags (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) UNIQUE NOT NULL,
  count INT NOT NULL DEFAULT 1
);
```

## Search Examples

### 1. Search for users starting with "john"
```
GET /api/search?q=john&type=users
GET /api/search?q=@john
```

### 2. Search for posts containing "travel"
```
GET /api/search?q=travel&type=posts
```

### 3. Search for hashtag "fitness"
```
GET /api/search?q=#fitness
GET /api/search/hashtags?q=fitness
```

### 4. Universal search
```
GET /api/search?q=hello
Returns: { users: [...], posts: [...], hashtags: [...] }
```

## Notification Navigation

### Click Behavior
- **FOLLOW/FOLLOW_REQUEST** → Navigate to user profile
- **LIKE/COMMENT/MENTION** → Navigate to dashboard (post feed)
- **MESSAGE** → Navigate to chat

### Implementation
```typescript
navigateToNotification(notification: Notification) {
  if (notification.type === 'FOLLOW' || notification.type === 'FOLLOW_REQUEST') {
    this.router.navigate(['/profile', notification.fromUsername]);
  } else if (notification.postId) {
    this.router.navigate(['/dashboard']);
  }
}
```

## Testing

### Test User Search
1. Navigate to search
2. Type "@john"
3. Should show only users with "john" in username

### Test Hashtag Search
1. Navigate to search
2. Type "#fitness"
3. Should show hashtag suggestions ordered by popularity
4. Should show posts containing #fitness

### Test Post Search
1. Navigate to search
2. Type "hello world"
3. Should show posts containing the keywords

### Test Notification Navigation
1. Get a like notification
2. Click on it
3. Should navigate to dashboard
4. Notification should be marked as read

## Notes
- Search is case-insensitive
- Results are limited for performance
- Hashtags are automatically extracted from posts
- Search service aggregates from multiple microservices
- All endpoints require JWT authentication
