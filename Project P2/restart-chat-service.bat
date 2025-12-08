@echo off
echo Restarting Chat Service...

REM Kill existing chat-service process
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8086" ^| find "LISTENING"') do taskkill /F /PID %%a 2>nul

echo Building chat-service...
cd "backend\chat-service"
call mvn clean install -DskipTests

echo Starting chat-service...
start "Chat Service" cmd /k "mvn spring-boot:run"

cd ..\..
echo Chat service restarted!
echo Check http://localhost:8086/actuator/health
pause
