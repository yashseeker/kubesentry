resource "aws_ecr_repository" "kubesentry" {
  name                 = "kubesentry"

  image_tag_mutability = "MUTABLE"
  image_scanning_configuration {
    scan_on_push = true
  }

  tags = local.common_tags
}