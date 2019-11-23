FROM maven:3.6.2-jdk-13 as build

COPY ./pom.xml .
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package

FROM openjdk:13-jdk

WORKDIR /usr/src/app
COPY --from=build target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]