#!/bin/bash

# Complete AWS setup script

set -e

echo "=== RevHub AWS Setup ==="

# Check prerequisites
if ! command -v terraform &> /dev/null; then
    echo "Error: Terraform not installed"
    exit 1
fi

if ! command -v aws &> /dev/null; then
    echo "Error: AWS CLI not installed"
    exit 1
fi

# Navigate to terraform directory
cd ../terraform

# Initialize Terraform
echo "Initializing Terraform..."
terraform init

# Validate configuration
echo "Validating Terraform configuration..."
terraform validate

# Plan infrastructure
echo "Planning infrastructure..."
terraform plan -out=tfplan

# Ask for confirmation
read -p "Do you want to apply this plan? (yes/no): " confirm
if [ "$confirm" != "yes" ]; then
    echo "Aborted"
    exit 0
fi

# Apply infrastructure
echo "Creating AWS infrastructure..."
terraform apply tfplan

# Get outputs
echo ""
echo "=== Infrastructure Created ==="
terraform output

echo ""
echo "Next steps:"
echo "1. Run ./build-and-push.sh to build and push Docker images"
echo "2. Run ./deploy-to-ec2.sh <EC2_IP> <KEY_FILE> to deploy"
