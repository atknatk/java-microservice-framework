#!/usr/bin/env bash

BASE_PATH=$(pwd)/..

source $BASE_PATH/api-gateway/src/main/docker/run.sh
source $BASE_PATH/eureka-server/src/main/docker/run.sh
source $BASE_PATH/uaa-service/src/main/docker/run.sh
#source $BASE_PATH/base-service/src/main/docker/run.sh
#source $BASE_PATH/message-service/src/main/docker/run.sh
#source $BASE_PATH/organization-service/src/main/docker/run.sh
