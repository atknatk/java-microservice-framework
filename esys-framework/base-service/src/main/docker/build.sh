#!/usr/bin/env bash
#bash ././../../../../scripts/build.sh
cp ../../../../dist/base-service*.jar service.jar
docker build -t=esys/base-service .
rm -rf service.jar
