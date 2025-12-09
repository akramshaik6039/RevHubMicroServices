@echo off
echo Stopping services...

echo Stopping User Service on port 8081...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081"') do taskkill /F /PID %%a 2>nul

echo Stopping Post Service on port 8082...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8082"') do taskkill /F /PID %%a 2>nul

timeout /t 3

echo Starting User Service...
cd backend\user-service
start "User Service" cmd /k "mvn spring-boot:run"
cd ..\..

timeout /t 5

echo Starting Post Service...
cd backend\post-service
start "Post Service" cmd /k "mvn spring-boot:run"
cd ..\..

echo Services restarted!
echo Check the console windows for logs.
pause
