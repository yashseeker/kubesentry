output "instance_public_ip" {
  value = aws_instance.kubesentry.public_ip
}

output "instance_public_dns" {
  value = aws_instance.kubesentry.public_dns
}

output "ecr_repository_url" {
  value = aws_ecr_repository.kubesentry.repository_url
}