# Final Setup Guide - RevHub Microservices

## ✅ Project Structure Complete

### Backend (9 Individual Spring Boot Projects)
- config-server (Port 8888)
- api-gateway (Port 8080)
- user-service (Port 8081)
- post-service (Port 8082)
- feed-service (Port 8083)
- follow-service (Port 8084)
- notification-service (Port 8085)
- chat-service (Port 8086)
- search-service (Port 8087)

### Frontend (6 Angular 18 Standalone Micro-Frontends)
- shell (Port 4200) - Host application
- mfe-auth (Port 4201) - Authentication
- mfe-feed (Port 4202) - Feed & Posts
- mfe-profile (Port 4203) - User Profiles
- mfe-chat (Port 4204) - Real-time Chat
- mfe-notifications (Port 4205) - Notifications

## 🚀 Installation & Setup

### Step 1: Install Frontend Dependencies

```bash
install-all.bat
```

This will install node_modules for all 6 micro-frontends.

### Step 2: Setup Databases

**MySQL:**
```sql
CREATE DATABASE revhub_users;
CREATE DATABASE revhub_posts;
CREATE DATABASE revhub_follows;
```

**MongoDB:**
Will auto-create databases on first connection.

### Step 3: Start Consul

```bash
consul agent -dev
```

### Step 4: Start Backend Services

```bash
start-all-services.bat
```

This starts all 9 Spring Boot microservices.

### Step 5: Start Frontend Micro-Frontends

```bash
start-all-mfes.bat
```

This starts all 6 Angular micro-frontends.

### Step 6: Access Application

Open browser: http://localhost:4200

## 📋 Features Implemented

### Backend
✅ JWT Authentication with email verification
✅ OTP verification via email
✅ Password reset functionality
✅ User profiles with file upload
✅ Posts with image/video upload
✅ Comments with nested replies
✅ Likes & Shares
✅ Hashtags & Mentions
✅ Follow/Unfollow system
✅ Personalized feed
✅ Real-time chat
✅ Notifications
✅ Search functionality
✅ Service discovery (Consul)
✅ API Gateway with JWT validation
✅ Feign client communication

### Frontend
✅ Module Federation architecture
✅ Independent micro-frontends
✅ Shared Angular dependencies
✅ Lazy loading
✅ Auth guard protection
✅ JWT interceptor
✅ File upload (profile pics, post media)
✅ Real-time updates
✅ Responsive design

## 🔧 Technology Stack

### Backend
- Spring Boot 3.5.8
- Spring Cloud Gateway
- Spring Cloud Consul
- Spring Cloud OpenFeign
- MySQL 8.0
- MongoDB 6.0
- JWT (jjwt 0.12.3)
- Spring Mail
- WebSocket

### Frontend
- Angular 18 (Standalone)
- Module Federation
- RxJS
- TypeScript 5.5
- Webpack 5

## 📁 Project Structure

```
Project P2/
├── backend/
│   ├── config-server/
│   ├── api-gateway/
│   ├── user-service/
│   ├── post-service/
│   ├── feed-service/
│   ├── follow-service/
│   ├── notification-service/
│   ├── chat-service/
│   └── search-service/
├── frontend/
│   ├── shell/
│   ├── mfe-auth/
│   ├── mfe-feed/
│   ├── mfe-profile/
│   ├── mfe-chat/
│   └── mfe-notifications/
├── config-repo/
├── install-all.bat
├── start-all-services.bat
└── start-all-mfes.bat
```

## 🎯 Verification Checklist

After installation, verify:

1. ✅ All backend services running (check Consul UI: http://localhost:8500)
2. ✅ All MFEs running (check terminal windows)
3. ✅ Shell loads at http://localhost:4200
4. ✅ Can register new user
5. ✅ Receive OTP email
6. ✅ Can verify OTP and login
7. ✅ Can create posts with images
8. ✅ Can comment and reply
9. ✅ Can like and share posts
10. ✅ Can follow/unfollow users
11. ✅ Can upload profile picture
12. ✅ Can view personalized feed

## 🔍 Troubleshooting

### Backend Issues
- Check MySQL is running (port 3306)
- Check MongoDB is running (port 27017)
- Check Consul is running (port 8500)
- Verify databases are created
- Check service logs in terminal windows

### Frontend Issues
- Run `install-all.bat` if node_modules missing
- Check all MFEs are running on correct ports
- Clear browser cache
- Check browser console for errors

### Email Issues
- SMTP credentials are configured in user-service
- Using Gmail: akramshaik6039@gmail.com
- Check spam folder for OTP emails

## 📊 Port Reference

| Service | Port | Type |
|---------|------|------|
| Config Server | 8888 | Backend |
| API Gateway | 8080 | Backend |
| User Service | 8081 | Backend |
| Post Service | 8082 | Backend |
| Feed Service | 8083 | Backend |
| Follow Service | 8084 | Backend |
| Notification Service | 8085 | Backend |
| Chat Service | 8086 | Backend |
| Search Service | 8087 | Backend |
| Shell | 4200 | Frontend |
| MFE-Auth | 4201 | Frontend |
| MFE-Feed | 4202 | Frontend |
| MFE-Profile | 4203 | Frontend |
| MFE-Chat | 4204 | Frontend |
| MFE-Notifications | 4205 | Frontend |
| Consul | 8500 | Infrastructure |
| MySQL | 3306 | Database |
| MongoDB | 27017 | Database |

## 🎉 Success!

Your complete microservices application with true micro-frontends is ready!

- Backend: 9 independent Spring Boot projects
- Frontend: 6 independent Angular 18 standalone apps
- Module Federation for dynamic loading
- Full feature parity with Project P1
