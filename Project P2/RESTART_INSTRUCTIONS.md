# RevHub - Restart Instructions

## Current Issue
Services are experiencing connection resets. This typically happens when:
- Services have been restarted multiple times
- Configuration changes haven't fully propagated
- Services are in an inconsistent state

## Solution: Clean Restart

### Step 1: Stop All Services
```bash
docker-compose down
```

### Step 2: Start All Services Fresh
```bash
docker-compose up -d
```

### Step 3: Wait for Startup (Important!)
Wait 2-3 minutes for all services to fully initialize.

### Step 4: Check Status
```bash
docker-compose ps
```

All services should show "Up" status.

### Step 5: Test
Open http://localhost in your browser

## What's Been Fixed

✅ Authentication - Login page shows first
✅ Suggestions - Gets users from database  
✅ Follow/Unfollow - Works correctly
✅ Notifications - Accept/reject follow requests
✅ Profile Pictures - File upload with 10MB limit
✅ Profile Posts - Shows user's posts
✅ File Persistence - Uploads saved to host machine

## Service URLs

- Frontend: http://localhost
- API Gateway: http://localhost:8080
- Consul UI: http://localhost:8500

## If Issues Persist

1. Check logs: `docker logs revhub-user-service`
2. Verify all ports are free before starting
3. Ensure no other applications are using ports 80, 8080-8087, 3306, 27017, 6379, 8500

## Quick Test Commands

```bash
# Test API Gateway
curl http://localhost:8080/actuator/health

# Test User Service
curl http://localhost:8081/actuator/health

# Test Follow Service  
curl http://localhost:8083/actuator/health

# Test Notification Service
curl http://localhost:8085/actuator/health
```

All should return: `{"status":"UP"}`
