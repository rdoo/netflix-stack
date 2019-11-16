FROM openjdk:13-jdk-alpine

WORKDIR /usr/src/app
COPY ./target/*.jar .

CMD ["java", "-jar", "auth-server-1.jar"]