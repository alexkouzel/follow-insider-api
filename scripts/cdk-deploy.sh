#!/bin/sh

# change to the fi-aws directory
cd fi-aws || exit

# deploy the stack
cdk deploy
