FROM openjdk:8-jdk-alpine
MAINTAINER William Bowen <willwbowen@gmail.com>
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/auth-service
COPY /target/salonapi-authentication-service*SNAPSHOT.jar /usr/local/auth-service/app.jar
COPY run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh