@echo off
echo Stopping all RevHub services...

REM Stop all Java processes (Spring Boot services)
echo Stopping Java/Spring Boot services...
taskkill /f /im java.exe 2>nul
if %errorlevel% == 0 (
    echo Java processes stopped
) else (
    echo No Java processes running
)

REM Stop Node.js processes (Angular frontend)
echo Stopping Node.js/Angular services...
taskkill /f /im node.exe 2>nul
if %errorlevel% == 0 (
    echo Node.js processes stopped
) else (
    echo No Node.js processes running
)

REM Stop Docker containers
echo Stopping Docker containers...
docker-compose down 2>nul
if %errorlevel% == 0 (
    echo Docker containers stopped
) else (
    echo No Docker containers running
)

REM Stop specific services by port
echo Checking for services on specific ports...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Stopping process on port 8080 (PID: %%a)
    taskkill /f /pid %%a 2>nul
)

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081 ^| findstr LISTENING') do (
    echo Stopping process on port 8081 (PID: %%a)
    taskkill /f /pid %%a 2>nul
)

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :4200 ^| findstr LISTENING') do (
    echo Stopping process on port 4200 (PID: %%a)
    taskkill /f /pid %%a 2>nul
)

echo.
echo All RevHub services have been stopped!
echo You can now sleep peacefully 😴
echo.
pause