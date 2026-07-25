resource "aws_security_group" "kubesentry_sg" {
  name        = "${var.project_name}-sg"
  description = "Security group for KubeSentry"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"

    # Replace this with your public IP later
    cidr_blocks = ["152.58.115.50/32"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = local.common_tags
}