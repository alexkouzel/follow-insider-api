## Deployment steps

1. Create IAM user
2. Create access key: {user} -> Security credentials -> Access keys -> Create access key
3. Configure AWS CLI by typing: `aws configure`
4. Export CERTIFICATE_ARN, which maps to SSL/TLS certificate issued by AWS
5. Export VPC_ID, which maps to the default VPC (e.g. for RDS)
6. Deploy CloudFormation using: `cdk bootstrap` `cdk deploy`