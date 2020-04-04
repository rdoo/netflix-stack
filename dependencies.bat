call mvn dependency:go-offline -f ./discovery-server/pom.xml
call mvn dependency:go-offline -f ./api-gateway/pom.xml
call mvn dependency:go-offline -f ./auth-server/pom.xml
call mvn dependency:go-offline -f ./user-service/pom.xml
call mvn dependency:go-offline -f ./file-service/pom.xml