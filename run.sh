#!/bin/sh

mvn spring-boot:run -f ./discovery-server/pom.xml &
mvn spring-boot:run -f ./api-gateway/pom.xml &
mvn spring-boot:run -f ./auth-server/pom.xml &
mvn spring-boot:run -f ./user-service/pom.xml &
mvn spring-boot:run -f ./image-service/pom.xml &
# mvn spring-boot:run -f ./note-service/pom.xml