FROM java:openjdk-8-jre-alpine

ENV SLEEP 0

#jar dosyasını kopyala
ADD *.jar /app.jar

RUN sh -c 'touch /app.war'
VOLUME /tmp
EXPOSE 7777
CMD echo "The application will start in ${SLEEP}s..." && \
    sleep ${SLEEP} && \
    java -jar /app.jar
