@echo off
echo ========================================
echo RevHub - Starting All Services
echo ========================================
echo.

echo [1/3] Starting Backend Services...
cd /d "e:\Project P2\Project P2"

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
echo [2/3] Backend services started!
echo.

echo [3/3] Starting Frontend...
start "Frontend" cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo All services are starting!
echo ========================================
echo.
echo Access points:
echo - Frontend: http://localhost:4200
echo - API Gateway: http://localhost:8080
echo - Consul UI: http://localhost:8500
echo.
echo Check individual terminal windows for service status
echo.
pause
