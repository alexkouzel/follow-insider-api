#!/bin/sh

. ./scripts/.env.sh

./gradlew :core:bootRun --args="--spring.profiles.active=$1"