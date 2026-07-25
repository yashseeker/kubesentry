resource "aws_iam_policy" "github_ecr_policy" {
  name        = "GitHubActionsECRPolicy"
  description = "Allow GitHub Actions to push images to the KubeSentry ECR repository"

  policy = jsonencode({
    Version = "2012-10-17"

    Statement = [
      {
        Effect = "Allow"

        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:CompleteLayerUpload",
          "ecr:InitiateLayerUpload",
          "ecr:UploadLayerPart",
          "ecr:PutImage",
          "ecr:BatchGetImage"
        ]

        Resource = "*"
      }
    ]
  })
}
resource "aws_iam_role_policy_attachment" "github_ecr_attach" {
  role       = aws_iam_role.github_actions.name
  policy_arn = aws_iam_policy.github_ecr_policy.arn
}
