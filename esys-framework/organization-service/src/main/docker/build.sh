#!/usr/bin/env bash
#bash ././../../../../scripts/build.sh
cp ../../../../dist/organization-service*.jar service.jar
docker build -t=esys/organization-service .
rm -rf service.jar
