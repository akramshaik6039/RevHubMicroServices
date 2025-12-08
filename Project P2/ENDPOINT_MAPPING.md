# RevHub Monolith to Microservices Endpoint Mapping

## Authentication Endpoints (User Service)

### Monolith: `/auth/*`
### Microservice: User Service (Port 8081) - `/api/auth/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/auth/login` | POST | `/api/auth/login` | ✅ Implemented |
| `/auth/register` | POST | `/api/auth/register` | ✅ Implemented |
| `/auth/logout` | POST | `/api/auth/logout` | ✅ Implemented |
| `/auth/forgot-password` | POST | `/api/auth/forgot-password` | ⚠️ Need to verify |
| `/auth/reset-password` | POST | `/api/auth/reset-password` | ⚠️ Need to verify |
| `/auth/send-verification` | POST | `/api/auth/send-verification` | ⚠️ Need to verify |
| `/auth/verify-email` | GET | `/api/auth/verify-email` | ⚠️ Need to verify |
| `/auth/verify-otp` | POST | `/api/auth/verify-otp` | ⚠️ Need to verify |

---

## Profile Endpoints (User Service)

### Monolith: `/profile/*`
### Microservice: User Service (Port 8081) - `/api/profile/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/profile/{username}` | GET | `/api/profile/{username}` | ✅ Implemented |
| `/profile` | PUT | `/api/profile` | ✅ Implemented |
| `/profile/all` | GET | `/api/profile/all` | ✅ Implemented |
| `/profile/search` | GET | `/api/profile/search` | ✅ Implemented |
| `/profile/{username}/posts` | GET | `/api/profile/{username}/posts` | ✅ Implemented |
| `/profile/follow/{username}` | POST | `/api/profile/follow/{username}` | ✅ Implemented |
| `/profile/unfollow/{username}` | DELETE | `/api/profile/unfollow/{username}` | ✅ Implemented |
| `/profile/follow-status/{username}` | GET | `/api/profile/follow-status/{username}` | ✅ Implemented |
| `/profile/follow-requests` | GET | `/api/profile/follow-requests` | ✅ Implemented |
| `/profile/follow-requests/{id}/accept` | POST | `/api/profile/follow-requests/{id}/accept` | ✅ Implemented |
| `/profile/follow-requests/{id}/reject` | POST | `/api/profile/follow-requests/{id}/reject` | ✅ Implemented |
| `/profile/cancel-request/{username}` | DELETE | `/api/profile/cancel-request/{username}` | ✅ Implemented |
| `/profile/{username}/followers` | GET | `/api/profile/{username}/followers` | ✅ Implemented |
| `/profile/{username}/following` | GET | `/api/profile/{username}/following` | ✅ Implemented |
| `/profile/remove-follower/{username}` | DELETE | `/api/profile/remove-follower/{username}` | ✅ Implemented |
| `/profile/upload-photo` | POST | `/api/profile/upload-photo` | ✅ Implemented |
| `/profile/suggestions` | GET | `/api/profile/suggestions` | ✅ Implemented |

---

## Post Endpoints (Post Service)

### Monolith: `/posts/*`
### Microservice: Post Service (Port 8082) - `/api/posts/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/posts` | GET | `/api/posts` | ✅ Implemented |
| `/posts/{id}` | GET | `/api/posts/{id}` | ✅ Implemented |
| `/posts/upload` | POST | `/api/posts/upload` | ✅ Implemented |
| `/posts` | POST | `/api/posts` | ✅ Implemented |
| `/posts/{id}` | PUT | `/api/posts/{id}` | ✅ Implemented |
| `/posts/{id}/media` | PUT | `/api/posts/{id}/media` | ⚠️ Need to implement |
| `/posts/{id}` | DELETE | `/api/posts/{id}` | ✅ Implemented |
| `/posts/search` | GET | `/api/posts/search` | ✅ Implemented |
| `/posts/user/{userId}` | GET | `/api/posts/user/{userId}` | ✅ Implemented |

---

## Like Endpoints (Post Service)

### Monolith: `/posts/{id}/like` or `/likes/*`
### Microservice: Post Service (Port 8082) - `/api/posts/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/posts/{id}/like` | POST | `/api/posts/{id}/like` | ✅ Implemented |
| `/posts/{id}/toggle-like` | POST | `/api/posts/{id}/toggle-like` | ✅ Implemented |

---

## Comment Endpoints (Post Service)

### Monolith: `/posts/{id}/comments` or `/comments/*`
### Microservice: Post Service (Port 8082) - `/api/posts/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/posts/{id}/comments` | GET | `/api/posts/{id}/comments` | ✅ Implemented |
| `/posts/{id}/comments` | POST | `/api/posts/{id}/comments` | ✅ Implemented |
| `/comments/{commentId}/replies` | POST | `/api/posts/comments/{commentId}/replies` | ✅ Implemented |
| `/comments/{commentId}` | DELETE | `/api/posts/comments/{commentId}` | ✅ Implemented |

---

## Share Endpoints (Post Service)

### Monolith: `/posts/{id}/share` or `/shares/*`
### Microservice: Post Service (Port 8082) - `/api/posts/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/posts/{id}/share` | POST | `/api/posts/{id}/share` | ✅ Implemented |

---

## Chat/Message Endpoints (Chat Service)

### Monolith: `/chat/*` or `/messages/*`
### Microservice: Chat Service (Port 8085) - `/api/chat/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/chat/contacts` | GET | `/api/chat/contacts` | ⚠️ Need to verify |
| `/chat/conversation/{username}` | GET | `/api/chat/conversation/{username}` | ⚠️ Need to verify |
| `/chat/send` | POST | `/api/chat/send` | ⚠️ Need to verify |
| `/chat/unread/{username}` | GET | `/api/chat/unread/{username}` | ⚠️ Need to verify |
| `/chat/mark-read/{username}` | POST | `/api/chat/mark-read/{username}` | ⚠️ Need to verify |

---

## Notification Endpoints (Notification Service)

### Monolith: `/notifications/*`
### Microservice: Notification Service (Port 8086) - `/api/notifications/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/notifications` | GET | `/api/notifications` | ⚠️ Need to verify |
| `/notifications/unread-count` | GET | `/api/notifications/unread-count` | ⚠️ Need to verify |
| `/notifications/{id}/read` | POST | `/api/notifications/{id}/read` | ⚠️ Need to verify |
| `/notifications/{id}` | DELETE | `/api/notifications/{id}` | ⚠️ Need to verify |

---

## Search Endpoints (Search Service)

### Monolith: `/search/*`
### Microservice: Search Service (Port 8087) - `/api/search/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/search/users` | GET | `/api/search/users` | ⚠️ Need to implement |
| `/search/posts` | GET | `/api/search/posts` | ⚠️ Need to implement |
| `/search/hashtags` | GET | `/api/search/hashtags` | ⚠️ Need to implement |

---

## Feed Endpoints (Feed Service)

### Monolith: `/feed/*` or part of `/posts`
### Microservice: Feed Service (Port 8083) - `/api/feed/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/posts?feedType=universal` | GET | `/api/feed/universal` | ⚠️ Need to verify |
| `/posts?feedType=followers` | GET | `/api/feed/followers` | ⚠️ Need to verify |

---

## Follow Endpoints (Follow Service)

### Monolith: Part of `/profile/*`
### Microservice: Follow Service (Port 8084) - `/api/follows/*`

| Monolith Endpoint | Method | Microservice Endpoint | Status |
|-------------------|--------|----------------------|--------|
| `/profile/follow/{username}` | POST | `/api/follows/{userId}` | ✅ Implemented |
| `/profile/unfollow/{username}` | DELETE | `/api/follows/{userId}` | ✅ Implemented |
| `/profile/{username}/following` | GET | `/api/follows/{userId}/following` | ✅ Implemented |
| `/profile/{username}/followers` | GET | `/api/follows/{userId}/followers` | ✅ Implemented |
| `/profile/follow-status/{username}` | GET | `/api/follows/status/{userId}` | ✅ Implemented |
| `/profile/follow-requests` | GET | `/api/follows/requests` | ✅ Implemented |
| `/profile/follow-requests/{id}/accept` | POST | `/api/follows/requests/{id}/accept` | ✅ Implemented |
| `/profile/follow-requests/{id}/reject` | POST | `/api/follows/requests/{id}/reject` | ✅ Implemented |
| `/profile/cancel-request/{username}` | DELETE | `/api/follows/cancel/{userId}` | ✅ Implemented |
| `/profile/remove-follower/{username}` | DELETE | `/api/follows/remove/{userId}` | ✅ Implemented |
| `/profile/suggestions` | GET | `/api/follows/suggestions` | ✅ Implemented |

---

## Priority Implementation Order

### Phase 1: Core Functionality (CURRENT)
1. ✅ Authentication (login, register)
2. ✅ Posts (create, read, update, delete)
3. ✅ Profile (view, update)
4. ⚠️ Follow/Unfollow (needs testing)
5. ✅ Comments
6. ✅ Likes

### Phase 2: Social Features
1. ⚠️ Chat/Messages
2. ⚠️ Notifications
3. ⚠️ Search (users, posts, hashtags)
4. ✅ Suggestions

### Phase 3: Advanced Features
1. Email verification
2. Password reset
3. Media upload optimization
4. Real-time updates

---

## Next Steps

1. **Test and fix follow functionality** - Currently getting 400 error
2. **Verify chat service endpoints**
3. **Verify notification service endpoints**
4. **Implement search service properly**
5. **Add missing endpoints from monolith**

---

## Notes

- All microservices use `X-User-Id` header for authentication
- API Gateway routes requests to appropriate services
- Frontend calls `/api/*` which is proxied to services
- Database schema aligned between monolith and microservices
