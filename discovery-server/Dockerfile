FROM openjdk:13-jdk-alpine

WORKDIR /usr/src/app
COPY ./target/*.jar .

CMD ["java", "-jar", "discovery-server-1.jar"]