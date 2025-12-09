@echo off
cls
echo ========================================
echo RevHub - Complete System Test Suite
echo ========================================
echo.
echo This will run all tests to verify your system
echo.
echo Tests included:
echo 1. Service Status Check
echo 2. Database Connectivity
echo 3. API Endpoints
echo 4. Detailed Functionality Test
echo.
pause

echo.
echo ========================================
echo RUNNING TEST SUITE
echo ========================================
echo.

echo [STEP 1/4] Service Status Check...
echo ========================================
call check-status.bat
echo.

echo.
echo [STEP 2/4] Database Connectivity Test...
echo ========================================
call test-databases.bat
echo.

echo.
echo [STEP 3/4] API Endpoints Test...
echo ========================================
call test-api-endpoints.bat
echo.

echo.
echo [STEP 4/4] Complete Functionality Test...
echo ========================================
call test-all-functionalities.bat
echo.

echo.
echo ========================================
echo ALL TESTS COMPLETED!
echo ========================================
echo.
echo Check the output above for any failures
echo.
pause
