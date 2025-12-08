@echo off
echo Restarting Post Service...

echo Stopping Post Service on port 8082...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8082"') do taskkill /F /PID %%a 2>nul

timeout /t 3

echo Cleaning and rebuilding Post Service...
cd backend\post-service
call mvn clean compile

timeout /t 2

echo Starting Post Service...
start "Post Service" cmd /k "mvn spring-boot:run"

cd ..\..
echo Post Service restarting...
pause
