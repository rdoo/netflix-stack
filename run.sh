#!/bin/sh

mvn -f ./discovery-server/pom.xml spring-boot:run &
mvn -f ./api-gateway/pom.xml spring-boot:run &
mvn -f ./note-service/pom.xml spring-boot:run