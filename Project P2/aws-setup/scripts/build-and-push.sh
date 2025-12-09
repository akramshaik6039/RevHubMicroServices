#!/bin/bash

# Build and push Docker images to ECR

set -e

AWS_REGION="ap-south-1"
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ECR_REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
PROJECT_NAME="revhub"

# Login to ECR
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}

# Services to build
SERVICES=("config-server" "api-gateway" "user-service" "post-service" "feed-service" "follow-service" "notification-service" "chat-service" "search-service")

cd "../../backend"

for SERVICE in "${SERVICES[@]}"; do
    echo "Building ${SERVICE}..."
    cd ${SERVICE}
    mvn clean package -DskipTests
    docker build -t ${PROJECT_NAME}-${SERVICE} .
    docker tag ${PROJECT_NAME}-${SERVICE}:latest ${ECR_REGISTRY}/${PROJECT_NAME}-${SERVICE}:latest
    docker push ${ECR_REGISTRY}/${PROJECT_NAME}-${SERVICE}:latest
    cd ..
done

# Build frontend
echo "Building frontend..."
cd ../frontend
docker build -t ${PROJECT_NAME}-frontend .
docker tag ${PROJECT_NAME}-frontend:latest ${ECR_REGISTRY}/${PROJECT_NAME}-frontend:latest
docker push ${ECR_REGISTRY}/${PROJECT_NAME}-frontend:latest

echo "All images pushed successfully!"
