@echo off
echo Starting RevHub Microservices...
echo.

echo Starting Config Server...
start "Config Server" cmd /k "cd backend\config-server && mvn spring-boot:run"
timeout /t 15

echo Starting API Gateway...
start "API Gateway" cmd /k "cd backend\api-gateway && mvn spring-boot:run"
timeout /t 10

echo Starting User Service...
start "User Service" cmd /k "cd backend\user-service && mvn spring-boot:run"
timeout /t 5

echo Starting Post Service...
start "Post Service" cmd /k "cd backend\post-service && mvn spring-boot:run"
timeout /t 5

echo Starting Feed Service...
start "Feed Service" cmd /k "cd backend\feed-service && mvn spring-boot:run"
timeout /t 5

echo Starting Follow Service...
start "Follow Service" cmd /k "cd backend\follow-service && mvn spring-boot:run"
timeout /t 5

echo Starting Notification Service...
start "Notification Service" cmd /k "cd backend\notification-service && mvn spring-boot:run"
timeout /t 5

echo Starting Chat Service...
start "Chat Service" cmd /k "cd backend\chat-service && mvn spring-boot:run"
timeout /t 5

echo Starting Search Service...
start "Search Service" cmd /k "cd backend\search-service && mvn spring-boot:run"
timeout /t 5

echo.
echo All services started!
echo Check Consul UI at http://localhost:8500
echo.
pause
