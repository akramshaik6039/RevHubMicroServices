# RevHub - Complete Testing Guide

## Quick Start Testing

### Option 1: Interactive Menu (Recommended)
```bash
START_HERE.bat
```
This provides an interactive menu to:
- Start all services
- Check status
- Run tests
- View URLs

### Option 2: Individual Test Scripts

#### 1. Quick Diagnostic
```bash
quick-diagnostic.bat
```
Checks:
- Java, Maven, Node.js versions
- Docker status
- Project structure
- Running services

#### 2. Service Status Check
```bash
check-status.bat
```
Verifies all services are running on correct ports

#### 3. Complete Functionality Test
```bash
test-all-functionalities.bat
```
Comprehensive test covering:
- Infrastructure (MySQL, MongoDB, Redis, Consul)
- All backend microservices
- Frontend
- Service registration
- API endpoints

#### 4. Database Connectivity Test
```bash
test-databases.bat
```
Tests connections to:
- MySQL
- MongoDB
- Redis

#### 5. API Endpoints Test
```bash
test-api-endpoints.bat
```
Tests actual API responses from all services

#### 6. Run All Tests
```bash
run-all-tests.bat
```
Executes all test scripts in sequence

## Service Ports Reference

| Service | Port | URL |
|---------|------|-----|
| Frontend | 4200 | http://localhost:4200 |
| API Gateway | 8080 | http://localhost:8080 |
| Config Server | 8888 | http://localhost:8888 |
| User Service | 8081 | http://localhost:8081 |
| Post Service | 8082 | http://localhost:8082 |
| Follow Service | 8083 | http://localhost:8083 |
| Feed Service | 8084 | http://localhost:8084 |
| Notification Service | 8085 | http://localhost:8085 |
| Chat Service | 8086 | http://localhost:8086 |
| Search Service | 8087 | http://localhost:8087 |
| Consul UI | 8500 | http://localhost:8500 |
| MySQL | 3306 | localhost:3306 |
| MongoDB | 27017 | localhost:27017 |
| Redis | 6379 | localhost:6379 |

## Testing Workflow

### 1. Before Starting
```bash
quick-diagnostic.bat
```
Ensure all prerequisites are installed

### 2. Start Services
Choose one method:

**Docker (Recommended):**
```bash
docker-compose up -d
```

**Manual:**
```bash
start-all.bat
```

### 3. Wait for Startup
Services need time to initialize (30-60 seconds)

### 4. Check Status
```bash
check-status.bat
```

### 5. Run Tests
```bash
test-all-functionalities.bat
```

### 6. Test APIs
```bash
test-api-endpoints.bat
```

## Expected Results

### All Services Running
- ✅ MySQL on port 3306
- ✅ MongoDB on port 27017
- ✅ Redis on port 6379
- ✅ Consul on port 8500
- ✅ Config Server on port 8888
- ✅ API Gateway on port 8080
- ✅ All microservices (8081-8087)
- ✅ Frontend on port 4200

### Health Checks
All services should return:
```json
{"status":"UP"}
```

### Consul Registration
Check registered services:
```bash
curl http://localhost:8500/v1/catalog/services
```

## Troubleshooting

### Service Not Starting
1. Check logs in service terminal
2. Verify database connections
3. Check port conflicts: `netstat -ano | findstr :<PORT>`
4. Restart service

### Database Connection Issues
1. Verify MySQL/MongoDB/Redis are running
2. Check credentials in application.yml
3. Test connection: `test-databases.bat`

### Port Already in Use
```bash
netstat -ano | findstr :<PORT>
taskkill /PID <PID> /F
```

### Frontend Not Loading
1. Check if backend services are running
2. Verify API Gateway is accessible
3. Check browser console for errors
4. Clear browser cache

## Manual API Testing

### Using curl

**Health Check:**
```bash
curl http://localhost:8080/actuator/health
```

**User Registration:**
```bash
curl -X POST http://localhost:8080/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password123\"}"
```

**Get Posts:**
```bash
curl http://localhost:8080/api/posts
```

### Using Browser
- Frontend: http://localhost:4200
- Consul UI: http://localhost:8500
- API Gateway: http://localhost:8080/actuator/health

## Continuous Testing

### Watch Service Status
```bash
:loop
call check-status.bat
timeout /t 30
goto loop
```

### Monitor Logs
Check individual service terminals or Docker logs:
```bash
docker-compose logs -f [service-name]
```

## Test Checklist

- [ ] All prerequisites installed (Java, Maven, Node.js)
- [ ] Databases running (MySQL, MongoDB, Redis)
- [ ] Consul running and accessible
- [ ] Config Server started
- [ ] API Gateway started
- [ ] All microservices started
- [ ] Services registered in Consul
- [ ] Frontend accessible
- [ ] API endpoints responding
- [ ] Health checks passing
- [ ] Can create user
- [ ] Can create post
- [ ] Can follow user
- [ ] Can view feed
- [ ] Can send notification
- [ ] Can chat
- [ ] Can search

## Performance Testing

Monitor resource usage:
```bash
docker stats
```

Check response times:
```bash
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/users
```

## Success Criteria

✅ All services show "PASS" in test-all-functionalities.bat
✅ All health endpoints return {"status":"UP"}
✅ Frontend loads without errors
✅ Can perform CRUD operations via API
✅ Services are registered in Consul
✅ No error logs in service terminals

## Next Steps After Testing

1. Review any failed tests
2. Check service logs for errors
3. Verify database schemas
4. Test user workflows in frontend
5. Monitor performance
6. Set up monitoring/alerting
7. Configure production settings
