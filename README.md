# Work In Progress
# Netflix Stack Microservices
Example project showcasing microservice architecture using Netflix stack

## Requirements
- JDK (tested on v.13)
- Maven (tested on v.3.6)
- MongoDB (tested on v.4.2)

OR
- Docker v.18 (Docker can automatically pull images of all the aforementioned dependencies)

## Project structure
Repository consists of 5 Spring Boot projects:
### Discovery server
Discovers and registers other microservices. Implemented using Netflix Eureka Server.
### API gateway
Single entry point that routes all requests to underlying microservices. Also acts as a load balancer. Implemented using Netflix Zuul.
### Authorization server
Handles user authorization. Implements OAuth2 password grant with access token in JWT format.
### User service
Microservice responsible for registering new users.
### File service
Example microservice that allows storing and retrieving files.

## Building and running
### Without Docker
TODO: MongoDB setup port 27017, 27018

Download dependencies, build and run by using `mvn spring-boot:run` in every project.
### With Docker
Run command `docker-compose up` in main directory.

## Features
- TODO
- mongodb, gridfs?
- oauth?
- jwt checked on gateway and services?
- documentation?

## Endpoints
TODO change localhost to `<your api gateway path>`
- Registering a user
```sh
curl --data "{\"username\":\"user\",\"password\":\"pass\"}" --header "Content-Type:application/json" http://localhost:8081/api/v1/users/register
```
- Logging in / Getting access token
```sh
curl --data "username=user&password=pass" http://localhost:8081/api/v1/login
```
- Refreshing access token
```sh
curl --data "refresh_token=<refresh_token from previous response>" http://localhost:8081/api/v1/refresh-token
```

TODO:
- REST documentation
- JWT keys
- project cleanup
- cloud config?