@echo off
echo Starting All Micro-Frontends...
echo.

echo Starting Shell (Port 4200)...
start "Shell" cmd /k "cd frontend\shell && npm start"
timeout /t 5

echo Starting MFE-Auth (Port 4201)...
start "MFE-Auth" cmd /k "cd frontend\mfe-auth && npm start"
timeout /t 3

echo Starting MFE-Feed (Port 4202)...
start "MFE-Feed" cmd /k "cd frontend\mfe-feed && npm start"
timeout /t 3

echo Starting MFE-Profile (Port 4203)...
start "MFE-Profile" cmd /k "cd frontend\mfe-profile && npm start"
timeout /t 3

echo Starting MFE-Chat (Port 4204)...
start "MFE-Chat" cmd /k "cd frontend\mfe-chat && npm start"
timeout /t 3

echo Starting MFE-Notifications (Port 4205)...
start "MFE-Notifications" cmd /k "cd frontend\mfe-notifications && npm start"

echo.
echo All Micro-Frontends Started!
echo Access the application at http://localhost:4200
echo.
pause
