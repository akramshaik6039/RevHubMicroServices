# RevHub AWS Deployment Guide

## Architecture Overview

- **VPC**: Custom VPC with public and private subnets
- **EC2**: t3.small instance for running Docker containers
- **RDS**: MySQL database (db.t3.micro)
- **ECR**: Container registry for all microservices
- **Security Groups**: Configured for EC2 and RDS access

## Prerequisites

1. AWS Account with appropriate permissions
2. AWS CLI installed and configured
3. Terraform installed (v1.0+)
4. Docker installed
5. EC2 key pair created in AWS Console

## Setup Steps

### 1. Configure Terraform Variables

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

Edit `terraform.tfvars`:
```
key_name     = "your-ec2-key-name"
db_password  = "your-secure-password"
```

### 2. Deploy Infrastructure

```bash
cd ../scripts
chmod +x setup-aws.sh
./setup-aws.sh
```

This will create:
- VPC with subnets
- EC2 instance
- RDS MySQL database
- ECR repositories
- Security groups

### 3. Build and Push Docker Images

```bash
chmod +x build-and-push.sh
./build-and-push.sh
```

### 4. Deploy to EC2

```bash
chmod +x deploy-to-ec2.sh
./deploy-to-ec2.sh <EC2_PUBLIC_IP> <PATH_TO_KEY_FILE>
```

Example:
```bash
./deploy-to-ec2.sh 13.232.45.67 ~/.ssh/my-key.pem
```

## Accessing the Application

After deployment:
- Frontend: `http://<EC2_PUBLIC_IP>`
- API Gateway: `http://<EC2_PUBLIC_IP>:8080`

## Database Connection

The RDS endpoint will be output after Terraform apply. Update your application configuration with:
- Host: `<RDS_ENDPOINT>`
- Port: `3306`
- Database: `revhub`
- Username: `admin`
- Password: `<your-password>`

## Cost Estimation

Monthly costs (approximate):
- EC2 t3.small: ~$15
- RDS db.t3.micro: ~$15
- Data transfer: Variable
- **Total: ~$30-40/month**

## Cleanup

To destroy all resources:
```bash
cd terraform
terraform destroy
```

## Troubleshooting

### SSH to EC2
```bash
ssh -i <key-file> ec2-user@<EC2_IP>
```

### Check Docker containers
```bash
docker ps
docker logs <container-name>
```

### View RDS logs
Check AWS Console → RDS → Logs

## Security Notes

- Change default passwords
- Restrict security group IPs in production
- Enable RDS encryption
- Use AWS Secrets Manager for credentials
