@echo off
echo ========================================
echo RevHub Services Status Check
echo ========================================
echo.

echo [1] Checking Consul (8500)...
netstat -ano | findstr ":8500" >nul
if %errorlevel% equ 0 (echo OK: Consul is running) else (echo FAILED: Consul not running)

echo [2] Checking Config Server (8888)...
netstat -ano | findstr ":8888" >nul
if %errorlevel% equ 0 (echo OK: Config Server is running) else (echo FAILED: Config Server not running)

echo [3] Checking API Gateway (8080)...
netstat -ano | findstr ":8080" >nul
if %errorlevel% equ 0 (echo OK: API Gateway is running) else (echo FAILED: API Gateway not running)

echo [4] Checking User Service (8081)...
netstat -ano | findstr ":8081" >nul
if %errorlevel% equ 0 (echo OK: User Service is running) else (echo FAILED: User Service not running)

echo [5] Checking Post Service (8082)...
netstat -ano | findstr ":8082" >nul
if %errorlevel% equ 0 (echo OK: Post Service is running) else (echo FAILED: Post Service not running)

echo [6] Checking Feed Service (8083)...
netstat -ano | findstr ":8083" >nul
if %errorlevel% equ 0 (echo OK: Feed Service is running) else (echo FAILED: Feed Service not running)

echo [7] Checking Follow Service (8084)...
netstat -ano | findstr ":8084" >nul
if %errorlevel% equ 0 (echo OK: Follow Service is running) else (echo FAILED: Follow Service not running)

echo [8] Checking Notification Service (8085)...
netstat -ano | findstr ":8085" >nul
if %errorlevel% equ 0 (echo OK: Notification Service is running) else (echo FAILED: Notification Service not running)

echo [9] Checking Chat Service (8086)...
netstat -ano | findstr ":8086" >nul
if %errorlevel% equ 0 (echo OK: Chat Service is running) else (echo FAILED: Chat Service not running)

echo [10] Checking Search Service (8087)...
netstat -ano | findstr ":8087" >nul
if %errorlevel% equ 0 (echo OK: Search Service is running) else (echo WARNING: Search Service not running - requires Elasticsearch)

echo [11] Checking Frontend (4200)...
netstat -ano | findstr ":4200" >nul
if %errorlevel% equ 0 (echo OK: Frontend is running) else (echo FAILED: Frontend not running)

echo.
echo [12] Checking MongoDB (27017)...
netstat -ano | findstr ":27017" >nul
if %errorlevel% equ 0 (echo OK: MongoDB is running) else (echo FAILED: MongoDB not running)

echo.
echo ========================================
echo Consul Services Registration:
echo ========================================
curl -s http://localhost:8500/v1/catalog/services

echo.
echo.
pause
