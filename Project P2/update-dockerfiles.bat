@echo off
cd backend
for /d %%d in (*-service api-gateway config-server) do (
    if exist "%%d\Dockerfile" (
        powershell -Command "(Get-Content '%%d\Dockerfile') -replace 'FROM openjdk:17-jdk-slim', 'FROM eclipse-temurin:17-jdk-alpine' | Set-Content '%%d\Dockerfile'"
        echo Updated %%d\Dockerfile
    )
)
echo All Dockerfiles updated!
