variable "aws_region" {
  description = "AWS region for deployment"
  type        = string
  default     = "ap-south-1"
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "kubesentry"
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "dev"
}
variable "key_pair_name" {
  description = "Existing EC2 Key Pair name"
  type        = string
  default     = "kubesentry-key"
}
variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}
variable "db_password" {
  description = "Postgres password"
  type        = string
  sensitive   = true
}