#!/bin/bash

dnf update -y

dnf install -y docker git java-21-amazon-corretto

systemctl enable docker
systemctl start docker

usermod -aG docker ec2-user

echo "Bootstrap completed" > /home/ec2-user/bootstrap.txt