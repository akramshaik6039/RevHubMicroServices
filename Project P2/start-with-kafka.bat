@echo off
echo ========================================
echo RevHub - Starting with Kafka
echo ========================================
echo.

echo [1/4] Starting Infrastructure (MySQL, MongoDB, Redis, Consul, Kafka)...
docker-compose up -d mysql mongodb redis consul zookeeper kafka
echo Waiting for services to be ready...
timeout /t 30

echo.
echo [2/4] Starting Config Server...
start "Config Server" cmd /k "cd backend\config-server && mvn spring-boot:run"
timeout /t 15

echo.
echo [3/4] Starting Backend Services...
start "API Gateway" cmd /k "cd backend\api-gateway && mvn spring-boot:run"
timeout /t 10

start "User Service" cmd /k "cd backend\user-service && mvn spring-boot:run"
timeout /t 5

start "Post Service" cmd /k "cd backend\post-service && mvn spring-boot:run"
timeout /t 5

start "Feed Service (Kafka Consumer)" cmd /k "cd backend\feed-service && mvn spring-boot:run"
timeout /t 5

start "Follow Service" cmd /k "cd backend\follow-service && mvn spring-boot:run"
timeout /t 5

start "Notification Service" cmd /k "cd backend\notification-service && mvn spring-boot:run"
timeout /t 5

start "Chat Service (Kafka Producer)" cmd /k "cd backend\chat-service && mvn spring-boot:run"
timeout /t 5

start "Search Service" cmd /k "cd backend\search-service && mvn spring-boot:run"
timeout /t 5

echo.
echo [4/4] Starting Frontend...
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
echo - Kafka: localhost:9092
echo.
echo Kafka Integration:
echo - Chat Service: Producer (publishes chat events)
echo - Feed Service: Consumer (receives chat events)
echo.
echo Check individual terminal windows for service status
echo.
pause
