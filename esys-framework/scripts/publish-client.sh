#!/usr/bin/env bash

cd ../ui

npm run build

tar -zcvf ../dist/ui-ti.tar.gz dist/ui/*
ssh root@test "rm -rf /usr/share/nginx/html/ti/*"
scp ../dist/ui-ti.tar.gz root@test:/tmp/ui-ti.tar.gz
ssh root@test "tar -zxvf /tmp/ui-ti.tar.gz -C /usr/share/nginx/html/ti/"
ssh root@test "mv /usr/share/nginx/html/ti/dist/ui/* /usr/share/nginx/html/ti/"
ssh root@test "rm -rf /usr/share/nginx/html/ti/dist"
