#!/usr/bin/env bash
#bash ././../../../../scripts/build.sh
cp ../../../../dist/message-service*.jar service.jar
docker build -t=esys/message-service .
rm -rf service.jar
