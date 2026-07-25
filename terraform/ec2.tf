resource "aws_instance" "kubesentry" {
  ami                    = data.aws_ami.amazon_linux.id
  instance_type          = var.instance_type
  key_name               = var.key_pair_name
  vpc_security_group_ids = [aws_security_group.kubesentry_sg.id]

  associate_public_ip_address = true

  user_data = file("${path.module}/user_data.sh")

  tags = merge(
    local.common_tags,
    {
      Name = "kubesentry-server"
    }
  )

  root_block_device {
    volume_size           = 20
    volume_type           = "gp3"
    delete_on_termination = true
  }
  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name
}