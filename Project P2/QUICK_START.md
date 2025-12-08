# Quick Start Guide

## Prerequisites Check

Before starting, ensure you have:

1. ✅ **Java 17+** installed
   ```bash
   java -version
   ```

2. ✅ **Maven 3.6+** installed
   ```bash
   mvn -version
   ```

3. ✅ **Node.js 18+** and npm installed
   ```bash
   node -version
   npm -version
   ```

4. ✅ **MySQL 8.0+** running
   - Username: root
   - Password: root
   - Port: 3306

5. ✅ **MongoDB** running
   - Port: 27017

6. ✅ **Consul** running
   ```bash
   consul agent -dev
   ```

## Step-by-Step Setup

### Step 1: Create MySQL Databases

Open MySQL command line or workbench and run:

```sql
CREATE DATABASE revhub_users;
CREATE DATABASE revhub_posts;
CREATE DATABASE revhub_follows;
```

### Step 2: Start Consul

Open a terminal and run:
```bash
consul agent -dev
```

Keep this terminal open. Consul UI will be available at: http://localhost:8500

### Step 3: Start Backend Services

**Option A: Use the startup script (Recommended)**
```bash
start-all-services.bat
```

**Option B: Start manually (one by one)**

Open separate terminals for each service:

```bash
# Terminal 1 - Config Server
cd backend/config-server
mvn spring-boot:run

# Terminal 2 - API Gateway (wait 15 seconds after Config Server)
cd backend/api-gateway
mvn spring-boot:run

# Terminal 3 - User Service (wait 10 seconds after Gateway)
cd backend/user-service
mvn spring-boot:run

# Terminal 4 - Post Service
cd backend/post-service
mvn spring-boot:run

# Terminal 5 - Feed Service
cd backend/feed-service
mvn spring-boot:run

# Terminal 6 - Follow Service
cd backend/follow-service
mvn spring-boot:run

# Terminal 7 - Notification Service
cd backend/notification-service
mvn spring-boot:run

# Terminal 8 - Chat Service
cd backend/chat-service
mvn spring-boot:run

# Terminal 9 - Search Service (Optional - requires Elasticsearch)
cd backend/search-service
mvn spring-boot:run
```

### Step 4: Start Frontend

Open a new terminal:
```bash
cd frontend/shell
npm install
npm start
```

Or use the script:
```bash
start-frontend.bat
```

### Step 5: Access the Application

Open your browser and navigate to:
```
http://localhost:4200
```

## Testing the Application

### 1. Register a New User
- Go to http://localhost:4200/auth/register
- Fill in the registration form
- Click "Register"

### 2. Login
- You'll be automatically logged in after registration
- Or go to http://localhost:4200/auth/login

### 3. Create a Post
- Go to the Feed page
- Type something in the text area
- Click "Post"

### 4. View Profile
- Click on "Profile" in the navigation
- See your posts and profile information

### 5. Test Chat
- Click on "Chat" in the navigation
- Send a test message

### 6. Check Notifications
- Click on "Notifications" in the navigation

## Verify Services are Running

Check Consul UI: http://localhost:8500/ui/dc1/services

You should see all services registered:
- config-server
- api-gateway
- user-service
- post-service
- feed-service
- follow-service
- notification-service
- chat-service
- search-service

## Service Ports

| Service | Port |
|---------|------|
| Config Server | 8888 |
| API Gateway | 8080 |
| User Service | 8081 |
| Post Service | 8082 |
| Feed Service | 8083 |
| Follow Service | 8084 |
| Notification Service | 8085 |
| Chat Service | 8086 |
| Search Service | 8087 |
| Frontend | 4200 |
| Consul | 8500 |

## Troubleshooting

### Services not starting?
- Check if ports are already in use
- Ensure MySQL and MongoDB are running
- Check Consul is running
- Verify Java and Maven versions

### Database connection errors?
- Verify MySQL credentials (root/root)
- Check if databases are created
- Ensure MySQL is running on port 3306

### Frontend not loading?
- Run `npm install` in frontend/shell directory
- Check if port 4200 is available
- Clear browser cache

### Services not registering with Consul?
- Ensure Consul is running on port 8500
- Check service logs for connection errors
- Restart the service

## API Testing with Postman/cURL

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Create Post (use token from login response)
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "content": "Hello from RevHub!",
    "hashtags": "#hello #revhub"
  }'
```

## Next Steps

1. Explore all features in the UI
2. Test inter-service communication
3. Monitor services in Consul
4. Check database tables for data
5. Prepare for Docker containerization

## Support

For issues or questions, check:
- Service logs in the terminal windows
- Consul UI for service health
- MySQL/MongoDB logs
- Browser console for frontend errors
