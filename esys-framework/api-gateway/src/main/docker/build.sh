#!/usr/bin/env bash
#bash ././../../../../scripts/build.sh
cp ../../../../dist/api-gateway*.jar service.jar
docker build -t=esys/api-gateway .
rm -rf service.jar
