variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-south-1"
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "revhub"
}

variable "key_name" {
  description = "EC2 key pair name"
  type        = string
}
