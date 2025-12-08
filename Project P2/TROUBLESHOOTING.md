# Troubleshooting Guide

## Current Status

### ✅ Running Services:
- Consul (8500)
- Config Server (8888)
- API Gateway (8080)
- User Service (8081)
- Post Service (8082)
- Feed Service (8083)
- Follow Service (8084)
- Notification Service (8085)
- Chat Service (8086)
- MongoDB (27017)

### ❌ Issues:

#### 1. MySQL Connection Error (500 Internal Server Error)
**Problem:** User Service, Post Service, and Follow Service are getting 500 errors when trying to register/create data.

**Cause:** MySQL is either:
- Not running
- Using different credentials than root/root
- Not accessible on localhost:3306

**Solution:**

**Option A: Start MySQL Service**
```cmd
net start MySQL80
```

**Option B: Check MySQL Credentials**
1. Open MySQL Workbench or command line
2. Try connecting with username: root, password: root
3. If it fails, update the credentials in these files:
   - `backend/user-service/src/main/resources/application.yml`
   - `backend/post-service/src/main/resources/application.yml`
   - `backend/follow-service/src/main/resources/application.yml`

**Option C: Create Databases Manually**
```sql
CREATE DATABASE IF NOT EXISTS revhub_users;
CREATE DATABASE IF NOT EXISTS revhub_posts;
CREATE DATABASE IF NOT EXISTS revhub_follows;
```

**Option D: Use H2 In-Memory Database (Quick Fix)**
If MySQL is not available, you can temporarily use H2 database:

1. Update `pom.xml` in user-service, post-service, follow-service:
```xml
<!-- Comment out MySQL -->
<!--
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
-->

<!-- Add H2 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: create-drop
```

#### 2. Frontend Not Running
**Problem:** Frontend on port 4200 is not accessible.

**Solution:**
```cmd
cd frontend
npm install
npm start
```

#### 3. Search Service Not Running (Port 8087)
**Problem:** Search Service requires Elasticsearch which is not running.

**Solution:**
- Either install and start Elasticsearch
- Or skip this service (it's optional for basic functionality)

## Quick Fix Commands

### Restart All Services:
```cmd
# Kill all Java processes
taskkill /F /IM java.exe /T

# Wait 5 seconds
timeout /t 5

# Start services again
start-all-services.bat
```

### Check Service Status:
```cmd
check-status.bat
```

### Test API Endpoints:

**Test User Service Directly:**
```cmd
curl http://localhost:8081/actuator/health
```

**Test Through API Gateway:**
```cmd
curl http://localhost:8080/api/auth/register -X POST -H "Content-Type: application/json" -d "{\"username\":\"test\",\"email\":\"test@test.com\",\"password\":\"test123\",\"fullName\":\"Test User\"}"
```

## Access Points

- **Frontend:** http://localhost:4200
- **API Gateway:** http://localhost:8080
- **Consul UI:** http://localhost:8500
- **Config Server:** http://localhost:8888

## Next Steps

1. Fix MySQL connection issue
2. Start frontend
3. Test registration and login
4. Verify all features work
