FROM openjdk:13-jdk-alpine

WORKDIR /usr/src/app
COPY ./target/*.jar .

CMD ["java", "-jar", "api-gateway-1.jar"]