#!/bin/sh
echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z configserver $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekaserver $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the database server to start on port $MYSQL_PORT"
echo "********************************************************"
while ! `nc -z database $MYSQL_PORT`; do sleep 3; done
echo "******** Database Server has started "

echo "********************************************************"
echo "Starting Authentication Service on port $AUTHSERVICE_PORT"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                               \
     -Dspring.cloud.config.password=$CONFIGSERVER_PASSWORD                    \
     -Dspring.profiles.active=$PROFILE                                         \
     -jar /usr/local/auth-service/app.jar