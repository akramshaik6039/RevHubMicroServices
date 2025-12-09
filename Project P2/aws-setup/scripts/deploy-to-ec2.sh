#!/bin/bash

# Deploy application to EC2 instance

set -e

EC2_IP=$1
KEY_FILE=$2

if [ -z "$EC2_IP" ] || [ -z "$KEY_FILE" ]; then
    echo "Usage: ./deploy-to-ec2.sh <EC2_IP> <KEY_FILE>"
    exit 1
fi

AWS_REGION="ap-south-1"
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ECR_REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

# Copy docker-compose file to EC2
scp -i ${KEY_FILE} ../../docker-compose.yml ec2-user@${EC2_IP}:~/

# SSH and deploy
ssh -i ${KEY_FILE} ec2-user@${EC2_IP} << EOF
    # Login to ECR
    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
    
    # Pull and run containers
    docker-compose pull
    docker-compose up -d
    
    echo "Deployment completed!"
EOF
