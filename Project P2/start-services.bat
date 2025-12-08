@echo off
cd backend\user-service
start cmd /k "mvn spring-boot:run"
timeout /t 5 /nobreak >nul

cd ..\api-gateway
start cmd /k "mvn spring-boot:run"
timeout /t 3 /nobreak >nul

cd ..\post-service
start cmd /k "mvn spring-boot:run"
timeout /t 3 /nobreak >nul

cd ..\feed-service
start cmd /k "mvn spring-boot:run"
timeout /t 3 /nobreak >nul

cd ..\follow-service
start cmd /k "mvn spring-boot:run"
timeout /t 3 /nobreak >nul

cd ..\notification-service
start cmd /k "mvn spring-boot:run"
timeout /t 3 /nobreak >nul

cd ..\chat-service
start cmd /k "mvn spring-boot:run"
timeout /t 3 /nobreak >nul

cd ..\search-service
start cmd /k "mvn spring-boot:run"

echo All services started!
