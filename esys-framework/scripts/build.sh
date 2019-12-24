#! /bin/sh
cd "`dirname "$0"`"
SETTINGSFILE="settings.xml"
LOCALREPO="localrepo"
set -e
export PATH=.:$PATH
rm -rf ../dist
mkdir ../dist

mvn325.sh clean install -f ../pom.xml -P publish,!default -DskipTests=true -s $SETTINGSFILE -Dmaven.repo.local="`pwd`/$LOCALREPO" "$@"

mvn clean package -P deploy,!default -DskipTests=true -Drelease.version=1.1.2
