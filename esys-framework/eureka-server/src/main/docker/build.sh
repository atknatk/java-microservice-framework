#!/usr/bin/env bash
#bash ././../../../../scripts/build.sh
cp ../../../../dist/eureka*.jar service.jar
docker build -t=esys/service-registry .
rm -rf service.jar
