#!/usr/bin/env bash
#bash ././../../../../scripts/build.sh
cp ../../../../dist/uaa-service*.jar service.jar
docker build -t=esys/uaa-service .
rm -rf service.jar
