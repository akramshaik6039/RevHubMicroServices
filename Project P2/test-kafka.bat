@echo off
echo ========================================
echo Testing Kafka Integration
echo ========================================
echo.

echo Checking Kafka container status...
docker ps | findstr kafka

echo.
echo Checking Zookeeper container status...
docker ps | findstr zookeeper

echo.
echo Checking Kafka logs (last 20 lines)...
docker logs --tail 20 revhub-kafka

echo.
echo ========================================
echo To test the integration:
echo 1. Send a chat message via API
echo 2. Check feed-service logs for consumed event
echo ========================================
pause
