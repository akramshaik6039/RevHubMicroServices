@echo off
echo Checking Prerequisites...
echo.

echo [1] Checking Java...
java -version 2>nul
if %errorlevel% neq 0 (
    echo FAILED: Java not found
) else (
    echo OK: Java installed
)
echo.

echo [2] Checking Maven...
mvn -version 2>nul
if %errorlevel% neq 0 (
    echo FAILED: Maven not found
) else (
    echo OK: Maven installed
)
echo.

echo [3] Checking Node.js...
node -v 2>nul
if %errorlevel% neq 0 (
    echo FAILED: Node.js not found
) else (
    echo OK: Node.js installed
)
echo.

echo [4] Checking npm...
npm -v 2>nul
if %errorlevel% neq 0 (
    echo FAILED: npm not found
) else (
    echo OK: npm installed
)
echo.

echo [5] Checking MySQL...
mysql --version 2>nul
if %errorlevel% neq 0 (
    echo WARNING: MySQL command not found in PATH
) else (
    echo OK: MySQL installed
)
echo.

echo [6] Checking Consul...
consul version 2>nul
if %errorlevel% neq 0 (
    echo WARNING: Consul not found
) else (
    echo OK: Consul installed
)
echo.

echo Prerequisites check complete!
pause
