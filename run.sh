#!/bin/sh

mvn -f ./discovery-server/pom.xml spring-boot:run &
mvn -f ./auth-server/pom.xml spring-boot:run &
mvn -f ./api-gateway/pom.xml spring-boot:run &
mvn -f ./user-service/pom.xml spring-boot:run &
mvn -f ./image-service/pom.xml spring-boot:run &
# mvn -f ./note-service/pom.xml spring-boot:run