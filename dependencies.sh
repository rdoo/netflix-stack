#!/bin/sh

mvn dependency:go-offline -f ./discovery-server/pom.xml
mvn dependency:go-offline -f ./api-gateway/pom.xml
mvn dependency:go-offline -f ./auth-server/pom.xml
mvn dependency:go-offline -f ./user-service/pom.xml
mvn dependency:go-offline -f ./image-service/pom.xml