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
Microservice responsible for registering new users. Uses MongoDB for data persistence.
### File service
Example microservice that allows storing and retrieving files. Files are kept in MongoDB GridFS collections allowing storing files larger than 16 MB. Uploading and downloading is streamed thanks to Apache Commons FileUpload library resulting in low memory profile.

## Building and running
Before running, it is important to create your own JWT encryption keys for security reasons.
To create a keystore use command:
```sh
keytool -genkeypair -keyalg RSA -keystore jwt.jks -alias jwt -storepass mypass -keypass mypass
```
and provide your own storepass and keypass and then copy `jwt.jks` file to auth-server resources directory and change storepass and keypass in auth-server `application.yml`.
Then export a public key using command:
```sh
keytool -list -rfc -keystore jwt.jks -alias jwt | openssl x509 -pubkey -noout > jwt-public-key.pem
```
and copy `jwt-public-key.pem` file content into api-gateway, user-service and file-service `application.yml` files.
### Running without Docker
TODO: MongoDB setup port 27017, 27018

Run command `mvn spring-boot:run` in every project directory.
### Running with Docker
Run command `docker-compose up` in main directory.

## Features
- TODO
- oauth?
- jwt checked on gateway and services?
- documentation?

## Endpoints
Eureka address: http://localhost:8082
User service docs: http://localhost:8084/swagger-ui.html
File service docs: http://localhost:8085/swagger-ui.html

TODO change localhost to `<your api gateway path>`
- Registering a user
```sh
curl --data "{\"username\":\"user\",\"password\":\"pass\",\"firstName\":\"First\",\"lastName\":\"Last\"}" --header "Content-Type:application/json" http://localhost:8081/api/v1/users/register
```
- Logging in / Getting access token
```sh
curl --data "{\"username\":\"user\",\"password\":\"pass\"}" --header "Content-Type:application/json" http://localhost:8081/api/v1/login
```
- Refreshing access token
```sh
curl --data "{\"refresh_token\":\"<refresh_token from login response>\"}" --header "Content-Type:application/json" http://localhost:8081/api/v1/refresh-token
```
- Uploading a file
```sh
curl --form file=@<path to your file> --header "Authorization:Bearer <your access_token>" http://localhost:8081/zuul/api/v1/files
```
- Listing files
```sh
curl --header "Authorization:Bearer <your access_token>" http://localhost:8081/api/v1/files
```
- Downloading a file
```sh
curl --header "Authorization:Bearer <your access_token>" http://localhost:8081/api/v1/files/<file id> --output <file name>
```
- Deleting a file
```sh
curl -X DELETE --header "Authorization:Bearer <your access_token>" http://localhost:8081/api/v1/files/<file id>
```

TODO:
- check poms
- check todos
- project cleanup
- cloud config? prob not