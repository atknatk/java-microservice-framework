#!/usr/bin/env bash
# Stop all
docker stop $(docker ps -a -q --filter="name=esys" --format="{{.ID}}")
# Delete all containers
docker rm -f $(docker ps -a -q -f "name=esys")
# Delete all images
docker images -a | awk '{ print $1,$2,$3 }' | grep "esys" | awk '{print $3 }' | xargs -I {} docker rmi {}
docker images -a | awk '{ print $1,$2,$3 }' | grep "none" | awk '{print $3 }' | xargs -I {} docker rmi {}
