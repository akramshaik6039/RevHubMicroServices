@echo off
cd backend\user-service
start "User Service" cmd /k "mvn spring-boot:run"
cd ..\..

cd backend\post-service
start "Post Service" cmd /k "mvn spring-boot:run"
cd ..\..

cd backend\follow-service
start "Follow Service" cmd /k "mvn spring-boot:run"
cd ..\..

cd backend\feed-service
start "Feed Service" cmd /k "mvn spring-boot:run"
cd ..\..

cd backend\notification-service
start "Notification Service" cmd /k "mvn spring-boot:run"
cd ..\..

cd backend\chat-service
start "Chat Service" cmd /k "mvn spring-boot:run"
cd ..\..

cd backend\api-gateway
start "API Gateway" cmd /k "mvn spring-boot:run"
cd ..\..

echo All services starting...
