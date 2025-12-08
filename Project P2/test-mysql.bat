@echo off
echo Testing MySQL Connection...
echo.

echo Attempting to connect to MySQL and create databases...
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS revhub_users; CREATE DATABASE IF NOT EXISTS revhub_posts; CREATE DATABASE IF NOT EXISTS revhub_follows; SHOW DATABASES LIKE 'revhub%%';"

if %errorlevel% equ 0 (
    echo SUCCESS: MySQL is running and databases are created!
) else (
    echo FAILED: Cannot connect to MySQL. Please check:
    echo 1. MySQL is running
    echo 2. Username is 'root'
    echo 3. Password is 'root'
    echo 4. MySQL is on port 3306
)

pause
