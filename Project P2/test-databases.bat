@echo off
echo ========================================
echo RevHub - Database Connectivity Test
echo ========================================
echo.

echo [1] Testing MySQL Connection...
echo ========================================
mysql -u root -proot -e "SHOW DATABASES;" 2>nul
if %errorlevel% equ 0 (
    echo [OK] MySQL is accessible
    echo.
    echo Checking RevHub databases:
    mysql -u root -proot -e "SHOW DATABASES LIKE 'revhub%%';"
    echo.
) else (
    echo [FAIL] Cannot connect to MySQL
    echo Make sure MySQL is running and credentials are correct
)

echo.
echo [2] Testing MongoDB Connection...
echo ========================================
mongosh --eval "db.adminCommand('ping')" 2>nul
if %errorlevel% equ 0 (
    echo [OK] MongoDB is accessible
    echo.
    echo Checking RevHub databases:
    mongosh --eval "db.adminCommand('listDatabases')" --quiet
    echo.
) else (
    echo [FAIL] Cannot connect to MongoDB
    echo Make sure MongoDB is running
)

echo.
echo [3] Testing Redis Connection...
echo ========================================
redis-cli ping 2>nul
if %errorlevel% equ 0 (
    echo [OK] Redis is accessible
    echo.
    echo Redis Info:
    redis-cli info server | findstr "redis_version"
    echo.
) else (
    echo [FAIL] Cannot connect to Redis
    echo Make sure Redis is running
)

echo.
echo ========================================
echo Database Test Complete!
echo ========================================
pause
