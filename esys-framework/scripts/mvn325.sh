#!/bin/sh


if [ "$TERM" = "cygwin" ] ; then
  S=';'
else
  S=':'
fi


OLD_M2_HOME="$M2_HOME"
export M2_HOME="$PWD/maven3/apache-maven-3.2.5"

OLD_PATH="$PATH"
OLD_MAVEN_OPTS="$MAVEN_OPTS"
export PATH=$M2_HOME/bin:$JAVA_HOME/bin:$PATH
export MAVEN_OPTS="-Xmx1024m"

mvn $*
EXITCODE=$?

export PATH="$OLD_PATH"
export M2_HOME="$OLD_M2_HOME"
export MAVEN_OPTS="$OLD_MAVEN_OPTS"

exit $EXITCODE
