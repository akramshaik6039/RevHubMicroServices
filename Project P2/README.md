# RevHub Microservices Project

A complete microservices-based social media platform built with Spring Boot 3.5.8 and Angular 18.

## Architecture

### Backend Microservices
- **Config Server** (Port 8888) - Centralized configuration management
- **API Gateway** (Port 8080) - Entry point with JWT authentication
- **User Service** (Port 8081) - Authentication, user profiles (MySQL)
- **Post Service** (Port 8082) - Posts, media, hashtags (MySQL)
- **Feed Service** (Port 8083) - Personalized feed with Redis caching
- **Follow Service** (Port 8084) - Follower graph management (MySQL)
- **Notification Service** (Port 8085) - Push notifications (MongoDB, WebSocket)
- **Chat Service** (Port 8086) - Real-time chat (MongoDB, WebSocket)
- **Search Service** (Port 8087) - Post & user search (Elasticsearch)

### Frontend
- **Shell Application** (Port 4200) - Main Angular 18 standalone application with micro-frontend architecture

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6+**
3. **Node.js 18+** and npm
4. **MySQL 8.0+** (username: root, password: root)
5. **MongoDB 6.0+**
6. **Consul 1.15+** (Service Discovery)
7. **Redis 7.0+** (Optional for Feed Service caching)
8. **Elasticsearch 8.0+** (Optional for Search Service)

## Setup Instructions

### 1. Install Consul
Download from: https://www.consul.io/downloads
```bash
# Windows
consul agent -dev
```

### 2. Setup Databases

**MySQL:**
```sql
CREATE DATABASE revhub_users;
CREATE DATABASE revhub_posts;
CREATE DATABASE revhub_follows;
```

**MongoDB:**
MongoDB will auto-create databases on first connection.

### 3. Start Backend Services

Start services in this order:

```bash
# 1. Config Server
cd backend/config-server
mvn spring-boot:run

# 2. API Gateway
cd backend/api-gateway
mvn spring-boot:run

# 3. User Service
cd backend/user-service
mvn spring-boot:run

# 4. Post Service
cd backend/post-service
mvn spring-boot:run

# 5. Feed Service
cd backend/feed-service
mvn spring-boot:run

# 6. Follow Service
cd backend/follow-service
mvn spring-boot:run

# 7. Notification Service
cd backend/notification-service
mvn spring-boot:run

# 8. Chat Service
cd backend/chat-service
mvn spring-boot:run

# 9. Search Service (if Elasticsearch is running)
cd backend/search-service
mvn spring-boot:run
```

### 4. Start Frontend

```bash
cd frontend/shell
npm install
npm start
```

Access the application at: http://localhost:4200

## API Endpoints

### Authentication (via API Gateway)
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - Login user

### Users
- GET `/api/users/{id}` - Get user by ID
- PUT `/api/users/{id}` - Update user profile
- GET `/api/users/search?q={query}` - Search users

### Posts
- POST `/api/posts` - Create post
- GET `/api/posts/{id}` - Get post
- GET `/api/posts/user/{userId}` - Get user posts
- POST `/api/posts/{id}/like` - Like post

### Feed
- GET `/api/feed` - Get personalized feed

### Follow
- POST `/api/follows/{userId}` - Follow user
- DELETE `/api/follows/{userId}` - Unfollow user
- GET `/api/follows/{userId}/following` - Get following list
- GET `/api/follows/{userId}/followers` - Get followers list

### Notifications
- GET `/api/notifications` - Get user notifications
- PUT `/api/notifications/{id}/read` - Mark as read

### Chat
- POST `/api/chat/send` - Send message
- GET `/api/chat/conversation/{userId}` - Get conversation

### Search
- GET `/api/search/posts?q={query}` - Search posts
- GET `/api/search/hashtag?tag={tag}` - Search by hashtag

## Service Discovery

All services register with Consul at `localhost:8500`

View registered services: http://localhost:8500/ui

## Technology Stack

### Backend
- Spring Boot 3.5.8
- Spring Cloud (Config, Gateway, Consul Discovery, OpenFeign)
- MySQL (User, Post, Follow data)
- MongoDB (Chat, Notifications)
- Redis (Feed caching)
- Elasticsearch (Search indexing)
- WebSocket (Real-time chat & notifications)
- JWT Authentication

### Frontend
- Angular 18 (Standalone components)
- RxJS
- HttpClient
- Router with lazy loading

## Development Notes

- All services use Consul for service discovery
- API Gateway handles authentication and routing
- JWT tokens are validated at the gateway level
- Services communicate via Feign clients
- Frontend uses lazy-loaded micro-frontend architecture

## Next Steps (Docker Implementation)

After testing the services locally, you can containerize them using Docker:
1. Create Dockerfile for each service
2. Create docker-compose.yml
3. Configure service networking
4. Add health checks
5. Setup volumes for data persistence
