#!/bin/sh

S3_BUCKET=elasticbeanstalk-eu-central-1-067416207435

ENV_NAME=fi-eb-env
S3_ASSET=fi-core.jar
APP_NAME=follow-insider

DATE=$(date "+%d-%m-%Y_%H:%M:%S")
VERSION=$APP_NAME_$DATE

# build the jar file
gradle build

# upload the jar file to S3
aws s3 cp fi-core/build/libs/$S3_ASSET s3://$S3_BUCKET/

# create a new version of the application
aws elasticbeanstalk create-application-version \
  --application-name $APP_NAME \
  --version-label $VERSION \
  --source-bundle S3Bucket=$S3_BUCKET,S3Key=$S3_ASSET

# update the environment to use the new version
aws elasticbeanstalk update-environment \
  --application-name $APP_NAME \
  --environment-name $ENV_NAME \
  --version-label $VERSION
