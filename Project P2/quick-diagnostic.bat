@echo off
cls
echo ========================================
echo RevHub - Quick Diagnostic
echo ========================================
echo.

:: Check Java
echo [1] Java Version:
java -version 2>&1 | findstr "version"
echo.

:: Check Maven
echo [2] Maven Version:
mvn -version 2>&1 | findstr "Apache Maven"
echo.

:: Check Node
echo [3] Node.js Version:
node -v 2>nul
if %errorlevel% neq 0 (echo Node.js not found!)
echo.

:: Check npm
echo [4] npm Version:
npm -v 2>nul
if %errorlevel% neq 0 (echo npm not found!)
echo.

:: Check Docker
echo [5] Docker Status:
docker --version 2>nul
if %errorlevel% equ 0 (
    docker ps 2>nul | findstr "CONTAINER"
    if !errorlevel! equ 0 (echo Docker is running) else (echo Docker installed but not running)
) else (
    echo Docker not found
)
echo.

:: Check running services
echo [6] Running Services on Key Ports:
echo ========================================
echo Port 3306 (MySQL):
netstat -ano | findstr ":3306" | findstr "LISTENING"
echo Port 27017 (MongoDB):
netstat -ano | findstr ":27017" | findstr "LISTENING"
echo Port 6379 (Redis):
netstat -ano | findstr ":6379" | findstr "LISTENING"
echo Port 8500 (Consul):
netstat -ano | findstr ":8500" | findstr "LISTENING"
echo Port 8888 (Config Server):
netstat -ano | findstr ":8888" | findstr "LISTENING"
echo Port 8080 (API Gateway):
netstat -ano | findstr ":8080" | findstr "LISTENING"
echo Port 4200 (Frontend):
netstat -ano | findstr ":4200" | findstr "LISTENING"
echo.

:: Check disk space
echo [7] Disk Space:
wmic logicaldisk get caption,freespace,size 2>nul | findstr "E:"
echo.

:: Check if project files exist
echo [8] Project Structure:
if exist "backend\config-server\pom.xml" (echo [OK] Config Server) else (echo [MISSING] Config Server)
if exist "backend\api-gateway\pom.xml" (echo [OK] API Gateway) else (echo [MISSING] API Gateway)
if exist "backend\user-service\pom.xml" (echo [OK] User Service) else (echo [MISSING] User Service)
if exist "backend\post-service\pom.xml" (echo [OK] Post Service) else (echo [MISSING] Post Service)
if exist "backend\follow-service\pom.xml" (echo [OK] Follow Service) else (echo [MISSING] Follow Service)
if exist "backend\feed-service\pom.xml" (echo [OK] Feed Service) else (echo [MISSING] Feed Service)
if exist "backend\notification-service\pom.xml" (echo [OK] Notification Service) else (echo [MISSING] Notification Service)
if exist "backend\chat-service\pom.xml" (echo [OK] Chat Service) else (echo [MISSING] Chat Service)
if exist "backend\search-service\pom.xml" (echo [OK] Search Service) else (echo [MISSING] Search Service)
if exist "frontend\package.json" (echo [OK] Frontend) else (echo [MISSING] Frontend)
echo.

echo ========================================
echo Diagnostic Complete!
echo ========================================
pause
