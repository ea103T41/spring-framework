# Spring Security Application

A spring security project with user authentication and path authorization.

## Dependencies
* JDK 17
* Spring Boot 3.2.4
* Spring Security
* MongoDB
* Maven 3

## Setup
### Prepare MongoDB
Run the following command to start a MongoDB container:
```
docker run -d --name "MongoDB_4.4.29" -p 27017:27017 mongo:4.4.29
```

## Usage
### Running the Application

- Directly using maven
```
mvn spring-boot:run
```

- From within your IDE right click run 
```
Application.java
```

- From executable jar file
```
mvn clean install
java -jar target/springboot-security-database-1.0-SNAPSHOT.war
```

# Reference
 - [在 Spring Security 整合資料庫進行認證](https://chikuwa-tech-study.blogspot.com/2023/11/spring-boot-security-authentication-integrating-with-mongodb-database.html)