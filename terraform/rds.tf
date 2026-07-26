resource "aws_db_subnet_group" "kubesentry" {
  name = "kubesentry-db-subnet-group"

  subnet_ids = [
    data.aws_subnets.default.ids[0],
    data.aws_subnets.default.ids[1]
  ]

  tags = {
    Name = "kubesentry-db-subnet-group"
  }
}

resource "aws_db_instance" "postgres" {
  identifier = "kubesentry-db"

  engine         = "postgres"
  engine_version = "17"

  instance_class = "db.t4g.micro"

  allocated_storage     = 20
  max_allocated_storage = 100

  db_name  = "kubesentry"
  username = "postgres"
  password = var.db_password

  publicly_accessible = false

  storage_encrypted       = true
  deletion_protection     = true
  backup_retention_period = 7
  skip_final_snapshot     = true

  vpc_security_group_ids = [
    aws_security_group.rds_sg.id
  ]

  db_subnet_group_name = aws_db_subnet_group.kubesentry.name
}