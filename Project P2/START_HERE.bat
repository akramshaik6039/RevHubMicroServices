@echo off
cls
echo ========================================
echo RevHub - System Startup Guide
echo ========================================
echo.
echo Current Status: Checking...
echo.

netstat -ano | findstr ":8080" >nul
if %errorlevel% equ 0 (
    echo [RUNNING] Services are already running!
    echo.
    goto :menu
) else (
    echo [STOPPED] No services detected
    echo.
)

:menu
echo What would you like to do?
echo.
echo 1. Start ALL services (Docker Compose - Recommended)
echo 2. Start services manually (Maven + npm)
echo 3. Check service status
echo 4. Test all functionalities
echo 5. View service URLs
echo 6. Stop all services
echo 7. Exit
echo.
choice /c 1234567 /n /m "Enter your choice (1-7): "

if errorlevel 7 goto :end
if errorlevel 6 goto :stop
if errorlevel 5 goto :urls
if errorlevel 4 goto :test
if errorlevel 3 goto :status
if errorlevel 2 goto :manual
if errorlevel 1 goto :docker

:docker
echo.
echo Starting services with Docker Compose...
echo ========================================
docker-compose up -d
echo.
echo Waiting for services to start (30 seconds)...
timeout /t 30 /nobreak
echo.
echo Services started! Checking status...
docker-compose ps
echo.
pause
goto :menu

:manual
echo.
echo Starting services manually...
echo ========================================
call start-all.bat
goto :menu

:status
echo.
echo Checking service status...
echo ========================================
call check-status.bat
goto :menu

:test
echo.
echo Running functionality tests...
echo ========================================
call test-all-functionalities.bat
goto :menu

:urls
cls
echo ========================================
echo RevHub - Service URLs
echo ========================================
echo.
echo FRONTEND:
echo   http://localhost:4200
echo.
echo BACKEND SERVICES:
echo   API Gateway:     http://localhost:8080
echo   Config Server:   http://localhost:8888
echo   User Service:    http://localhost:8081
echo   Post Service:    http://localhost:8082
echo   Follow Service:  http://localhost:8083
echo   Feed Service:    http://localhost:8084
echo   Notification:    http://localhost:8085
echo   Chat Service:    http://localhost:8086
echo   Search Service:  http://localhost:8087
echo.
echo INFRASTRUCTURE:
echo   Consul UI:       http://localhost:8500
echo   MySQL:           localhost:3306
echo   MongoDB:         localhost:27017
echo   Redis:           localhost:6379
echo.
pause
goto :menu

:stop
echo.
echo Stopping all services...
echo ========================================
docker-compose down
echo.
echo Services stopped!
pause
goto :menu

:end
echo.
echo Goodbye!
exit
