@echo off
echo ========================================
echo Installing All Frontend Dependencies
echo ========================================
echo.

echo [1/6] Installing Shell...
cd frontend\shell
call npm install
cd ..\..

echo.
echo [2/6] Installing MFE-Auth...
cd frontend\mfe-auth
call npm install
cd ..\..

echo.
echo [3/6] Installing MFE-Feed...
cd frontend\mfe-feed
call npm install
cd ..\..

echo.
echo [4/6] Installing MFE-Profile...
cd frontend\mfe-profile
call npm install
cd ..\..

echo.
echo [5/6] Installing MFE-Chat...
cd frontend\mfe-chat
call npm install
cd ..\..

echo.
echo [6/6] Installing MFE-Notifications...
cd frontend\mfe-notifications
call npm install
cd ..\..

echo.
echo ========================================
echo All Dependencies Installed Successfully!
echo ========================================
echo.
echo Next Steps:
echo 1. Start backend services: start-all-services.bat
echo 2. Start frontend MFEs: start-all-mfes.bat
echo.
pause
