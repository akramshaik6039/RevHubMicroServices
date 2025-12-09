# AWS Infrastructure Setup with Terraform

## Prerequisites
- AWS CLI configured with credentials
- Terraform installed
- EC2 key pair created in AWS

## Setup Steps

1. Copy the example variables file:
```bash
cp terraform.tfvars.example terraform.tfvars
```

2. Edit `terraform.tfvars` with your values:
```
key_name = "your-actual-key-pair-name"
```

3. Initialize Terraform:
```bash
terraform init
```

4. Review the plan:
```bash
terraform plan
```

5. Apply the configuration:
```bash
terraform apply
```

## Resources Created
- VPC with public subnet
- Internet Gateway
- EC2 instance (t3.small) with Docker installed
- Security groups for EC2
- ECR repositories for all microservices

## Outputs
After applying, you'll get:
- EC2 public IP
- ECR repository URLs
- VPC ID

## Cleanup
To destroy all resources:
```bash
terraform destroy
```
