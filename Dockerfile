FROM openjdk:8-jre-alpine

RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

WORKDIR /usr/share/api
ADD /build/libs/*.jar .

EXPOSE 7000
CMD java -jar *.jar
