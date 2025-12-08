@echo off
echo Stopping all backend services...

for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081"') do taskkill /F /PID %%a 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8082"') do taskkill /F /PID %%a 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8084"') do taskkill /F /PID %%a 2>nul

timeout /t 3

echo Starting User Service...
start "User Service" cmd /k "cd backend\user-service && java -jar target\user-service-1.0.0.jar"

timeout /t 5

echo Starting Post Service...
start "Post Service" cmd /k "cd backend\post-service && java -jar target\post-service-1.0.0.jar"

timeout /t 5

echo Starting Follow Service...
start "Follow Service" cmd /k "cd backend\follow-service && java -jar target\follow-service-1.0.0.jar"

echo All services starting...
pause
