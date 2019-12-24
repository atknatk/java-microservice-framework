#!/usr/bin/env bash
docker start esys-api-gateway
docker start esys-service-registry
docker start esys-uaa-service
#docker start esys-base-service
#docker start esys-message-service
#docker start esys-organization-service
echo "starting sevices..."
