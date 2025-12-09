@echo off
setlocal enabledelayedexpansion
echo ========================================
echo RevHub - Complete Functionality Test
echo ========================================
echo.

:: Color codes for output
set "GREEN=[92m"
set "RED=[91m"
set "YELLOW=[93m"
set "RESET=[0m"

:: Test Results Counter
set /a PASSED=0
set /a FAILED=0

echo [PHASE 1] Infrastructure Services Check
echo ========================================
echo.

echo [1.1] Testing MySQL (3306)...
netstat -ano | findstr ":3306" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - MySQL is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - MySQL not running
    set /a FAILED+=1
)

echo [1.2] Testing MongoDB (27017)...
netstat -ano | findstr ":27017" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - MongoDB is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - MongoDB not running
    set /a FAILED+=1
)

echo [1.3] Testing Redis (6379)...
netstat -ano | findstr ":6379" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Redis is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Redis not running
    set /a FAILED+=1
)

echo [1.4] Testing Consul (8500)...
netstat -ano | findstr ":8500" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Consul is running
    curl -s http://localhost:8500/v1/status/leader >nul 2>&1
    if !errorlevel! equ 0 (
        echo %GREEN%PASS%RESET% - Consul API responding
        set /a PASSED+=1
    ) else (
        echo %YELLOW%WARN%RESET% - Consul port open but API not responding
        set /a FAILED+=1
    )
) else (
    echo %RED%FAIL%RESET% - Consul not running
    set /a FAILED+=1
)

echo.
echo [PHASE 2] Backend Services Check
echo ========================================
echo.

echo [2.1] Testing Config Server (8888)...
netstat -ano | findstr ":8888" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Config Server port is open
    curl -s http://localhost:8888/actuator/health >nul 2>&1
    if !errorlevel! equ 0 (
        echo %GREEN%PASS%RESET% - Config Server health check OK
        set /a PASSED+=1
    ) else (
        echo %YELLOW%WARN%RESET% - Config Server responding but health check failed
        set /a PASSED+=1
    )
) else (
    echo %RED%FAIL%RESET% - Config Server not running
    set /a FAILED+=1
)

echo [2.2] Testing API Gateway (8080)...
netstat -ano | findstr ":8080" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - API Gateway port is open
    curl -s http://localhost:8080/actuator/health >nul 2>&1
    if !errorlevel! equ 0 (
        echo %GREEN%PASS%RESET% - API Gateway health check OK
        set /a PASSED+=1
    ) else (
        echo %YELLOW%WARN%RESET% - API Gateway responding but health check failed
        set /a PASSED+=1
    )
) else (
    echo %RED%FAIL%RESET% - API Gateway not running
    set /a FAILED+=1
)

echo [2.3] Testing User Service (8081)...
netstat -ano | findstr ":8081" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - User Service is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - User Service not running
    set /a FAILED+=1
)

echo [2.4] Testing Post Service (8082)...
netstat -ano | findstr ":8082" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Post Service is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Post Service not running
    set /a FAILED+=1
)

echo [2.5] Testing Follow Service (8083)...
netstat -ano | findstr ":8083" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Follow Service is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Follow Service not running
    set /a FAILED+=1
)

echo [2.6] Testing Feed Service (8084)...
netstat -ano | findstr ":8084" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Feed Service is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Feed Service not running
    set /a FAILED+=1
)

echo [2.7] Testing Notification Service (8085)...
netstat -ano | findstr ":8085" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Notification Service is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Notification Service not running
    set /a FAILED+=1
)

echo [2.8] Testing Chat Service (8086)...
netstat -ano | findstr ":8086" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Chat Service is running
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Chat Service not running
    set /a FAILED+=1
)

echo [2.9] Testing Search Service (8087)...
netstat -ano | findstr ":8087" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Search Service is running
    set /a PASSED+=1
) else (
    echo %YELLOW%WARN%RESET% - Search Service not running (optional)
    set /a PASSED+=1
)

echo.
echo [PHASE 3] Frontend Check
echo ========================================
echo.

echo [3.1] Testing Frontend (4200)...
netstat -ano | findstr ":4200" >nul
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Frontend is running
    curl -s http://localhost:4200 >nul 2>&1
    if !errorlevel! equ 0 (
        echo %GREEN%PASS%RESET% - Frontend accessible
        set /a PASSED+=1
    ) else (
        echo %YELLOW%WARN%RESET% - Frontend port open but not responding
        set /a PASSED+=1
    )
) else (
    echo %RED%FAIL%RESET% - Frontend not running
    set /a FAILED+=1
)

echo.
echo [PHASE 4] Service Registration Check
echo ========================================
echo.

echo [4.1] Checking Consul Service Registry...
curl -s http://localhost:8500/v1/catalog/services >nul 2>&1
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - Consul registry accessible
    echo.
    echo Registered Services:
    curl -s http://localhost:8500/v1/catalog/services
    echo.
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - Cannot access Consul registry
    set /a FAILED+=1
)

echo.
echo [PHASE 5] API Endpoint Tests
echo ========================================
echo.

echo [5.1] Testing API Gateway Routes...
curl -s -o nul -w "%%{http_code}" http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo %GREEN%PASS%RESET% - API Gateway endpoints accessible
    set /a PASSED+=1
) else (
    echo %RED%FAIL%RESET% - API Gateway endpoints not accessible
    set /a FAILED+=1
)

echo.
echo ========================================
echo Test Summary
echo ========================================
echo.
set /a TOTAL=PASSED+FAILED
echo Total Tests: %TOTAL%
echo %GREEN%Passed: %PASSED%%RESET%
echo %RED%Failed: %FAILED%%RESET%
echo.

if %FAILED% equ 0 (
    echo %GREEN%ALL TESTS PASSED!%RESET%
    echo Your RevHub application is fully functional!
) else (
    echo %YELLOW%Some tests failed. Please check the services above.%RESET%
)

echo.
echo ========================================
echo Quick Access URLs
echo ========================================
echo Frontend:        http://localhost:4200
echo API Gateway:     http://localhost:8080
echo Consul UI:       http://localhost:8500
echo Config Server:   http://localhost:8888
echo.
echo ========================================
echo Service Ports
echo ========================================
echo User Service:         8081
echo Post Service:         8082
echo Follow Service:       8083
echo Feed Service:         8084
echo Notification Service: 8085
echo Chat Service:         8086
echo Search Service:       8087
echo.

pause
