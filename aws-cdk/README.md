## Elastic Beanstalk deployment

1. Create IAM user

2. Create access key for this user:
   {user} -> Security credentials -> Access keys -> Create access key

3. Configure AWS CLI using by typing:
   ```bath
   aws configure
   ```

4. Export CERTIFICATE_ARN, which maps to SSL/TLS certificate issued by Amazon

5. Export VPC_ID, which maps to the default VPC (to use other AWS services outside
   Elastic Beanstalk e.g. RDS)

6. Deploy CloudFormation using:
   ```bash
   cdk bootstrap
   ```
   ```bash
   cdk synth
   ```
   ```bash
   cdk deploy
   ```