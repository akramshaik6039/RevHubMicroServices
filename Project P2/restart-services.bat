@echo off
echo Restarting User Service and Feed Service...

for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081"') do taskkill /F /PID %%a 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8083"') do taskkill /F /PID %%a 2>nul

timeout /t 5

start "User Service" cmd /k "cd backend\user-service && mvn spring-boot:run"
timeout /t 5
start "Feed Service" cmd /k "cd backend\feed-service && mvn spring-boot:run"

echo Services restarting...
pause
