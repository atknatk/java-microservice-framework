#!/usr/bin/env bash

./build.sh

# config server her seferinde publish edilmemeli

#scp ../dist/config-server*.jar root@test:/opt/fw/config-server/app.jar
#ssh root@test 'systemctl restart config-server'

#scp ../dist/api-gateway*.jar root@test:/opt/fw/api-gateway/app.jar
#ssh root@test 'systemctl restart api-gateway'

#scp ../dist/eureka-server*.jar root@test:/opt/fw/eureka-server/app.jar
#ssh root@test 'systemctl restart eureka-server'

scp ../dist/uaa-service*.jar root@test:/opt/fw/uaa-service/app.jar
#ssh root@test 'systemctl restart uaa'
ssh root@test 'systemctl restart uaa-8051'

scp ../dist/organization-service*.jar root@test:/opt/fw/organization-service/app.jar
ssh root@test 'systemctl restart organization'

scp ../dist/base-service*.jar root@test:/opt/fw/base-service/app.jar
ssh root@test 'systemctl restart base'

scp ../dist/message-service*.jar root@test:/opt/fw/message-service/app.jar
ssh root@test 'systemctl restart message'
