#!/usr/bin/env bash

BASE_PATH=$(pwd)/..

bash build.sh

cd $BASE_PATH/api-gateway/src/main/docker
source build.sh

cd $BASE_PATH/eureka-server/src/main/docker
source build.sh

cd $BASE_PATH/uaa-service/src/main/docker
source build.sh


#cd $BASE_PATH/base-service/src/main/docker
#source build.sh
#
#cd $BASE_PATH/message-service/src/main/docker
#source build.sh
#
#cd $BASE_PATH/organization-service/src/main/docker
#source build.sh
#
