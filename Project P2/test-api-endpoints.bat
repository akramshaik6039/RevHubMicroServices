@echo off
echo ========================================
echo RevHub - API Endpoints Functionality Test
echo ========================================
echo.
echo This script tests actual API endpoints
echo Make sure all services are running!
echo.
pause

echo.
echo [TEST 1] Config Server - Get Configuration
echo ========================================
curl -s http://localhost:8888/actuator/health
echo.

echo.
echo [TEST 2] API Gateway - Health Check
echo ========================================
curl -s http://localhost:8080/actuator/health
echo.

echo.
echo [TEST 3] User Service - Health Check
echo ========================================
curl -s http://localhost:8081/actuator/health
echo.

echo.
echo [TEST 4] Post Service - Health Check
echo ========================================
curl -s http://localhost:8082/actuator/health
echo.

echo.
echo [TEST 5] Follow Service - Health Check
echo ========================================
curl -s http://localhost:8083/actuator/health
echo.

echo.
echo [TEST 6] Feed Service - Health Check
echo ========================================
curl -s http://localhost:8084/actuator/health
echo.

echo.
echo [TEST 7] Notification Service - Health Check
echo ========================================
curl -s http://localhost:8085/actuator/health
echo.

echo.
echo [TEST 8] Chat Service - Health Check
echo ========================================
curl -s http://localhost:8086/actuator/health
echo.

echo.
echo [TEST 9] Search Service - Health Check
echo ========================================
curl -s http://localhost:8087/actuator/health
echo.

echo.
echo [TEST 10] Consul - List All Services
echo ========================================
curl -s http://localhost:8500/v1/catalog/services | python -m json.tool 2>nul
if %errorlevel% neq 0 (
    curl -s http://localhost:8500/v1/catalog/services
)
echo.

echo.
echo [TEST 11] API Gateway - Test User Service Route
echo ========================================
curl -s http://localhost:8080/api/users/health 2>nul
if %errorlevel% neq 0 (
    echo Route may not be configured or service not responding
)
echo.

echo.
echo [TEST 12] API Gateway - Test Post Service Route
echo ========================================
curl -s http://localhost:8080/api/posts/health 2>nul
if %errorlevel% neq 0 (
    echo Route may not be configured or service not responding
)
echo.

echo.
echo ========================================
echo API Testing Complete!
echo ========================================
echo.
echo If you see JSON responses above, your APIs are working!
echo If you see errors, check the service logs.
echo.
pause
