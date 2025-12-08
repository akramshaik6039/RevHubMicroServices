# Project Structure

## Backend Microservices

```
backend/
├── config-server/          # Centralized configuration (Port 8888)
├── api-gateway/            # API Gateway with JWT auth (Port 8080)
├── user-service/           # User management (Port 8081)
├── post-service/           # Post management (Port 8082)
├── feed-service/           # Personalized feed (Port 8083)
├── follow-service/         # Follow relationships (Port 8084)
├── notification-service/   # Notifications (Port 8085)
├── chat-service/           # Real-time chat (Port 8086)
└── search-service/         # Search functionality (Port 8087)
```

## Frontend Micro-Frontend

```
frontend/
└── shell/                  # Main Angular 18 app (Port 4200)
    ├── src/
    │   ├── app/
    │   │   ├── core/
    │   │   │   ├── services/
    │   │   │   ├── guards/
    │   │   │   └── interceptors/
    │   │   └── features/
    │   │       ├── auth/
    │   │       ├── feed/
    │   │       ├── profile/
    │   │       ├── chat/
    │   │       └── notifications/
    │   ├── index.html
    │   ├── main.ts
    │   └── styles.css
    └── package.json
```

## Configuration Repository

```
config-repo/
└── application.yml         # Shared configuration
```

## Technology Stack by Service

### User Service
- Spring Boot 3.5.8
- Spring Data JPA
- MySQL
- Spring Security
- JWT
- Consul Discovery
- OpenFeign

### Post Service
- Spring Boot 3.5.8
- Spring Data JPA
- MySQL
- Consul Discovery
- OpenFeign

### Feed Service
- Spring Boot 3.5.8
- Redis (caching)
- Consul Discovery
- OpenFeign

### Follow Service
- Spring Boot 3.5.8
- Spring Data JPA
- MySQL
- Consul Discovery

### Notification Service
- Spring Boot 3.5.8
- MongoDB
- WebSocket
- Consul Discovery

### Chat Service
- Spring Boot 3.5.8
- MongoDB
- WebSocket
- Consul Discovery

### Search Service
- Spring Boot 3.5.8
- Elasticsearch
- Consul Discovery
- OpenFeign
```
