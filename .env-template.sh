#!/bin/sh

### GENERAL ###
export SERVER_PORT=
export EDGAR_USER_AGENT=

### AWS ###
export S3_BUCKET=

### GITHUB ###
export GPR_USERNAME=
export GPR_TOKEN=

### EMAIL ###
export EMAIL_USERNAME=
export EMAIL_PASSWORD=
export EMAIL_SENDER=

### ADMIN ###
export PROD_ADMIN_USERNAME=
export PROD_ADMIN_PASSWORD=

export DEV_ADMIN_USERNAME=
export DEV_ADMIN_PASSWORD=

### DATABASE ###
export TEST_DB_URL='jdbc:h2:mem:db'
export TEST_DB_USERNAME='sa'
export TEST_DB_PASSWORD='password'
export TEST_DB_DRIVER='org.h2.Driver'

export PROD_DB_URL=
export PROD_DB_USERNAME=
export PROD_DB_PASSWORD=
export PROD_DB_DRIVER='org.postgresql.Driver'

export DEV_DB_URL=
export DEV_DB_USERNAME=
export DEV_DB_PASSWORD=
export DEV_DB_DRIVER='org.postgresql.Driver'
