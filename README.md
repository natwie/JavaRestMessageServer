# JavaRestMessageServer
This short document describes the installation procedure and available commands in text messaging app

## Third party libraries

* Spring Boot
* Hibernate ORM
* SQLite
* Embedded Apache Tomcat
* JUnit

## Running

### Prerequisities for running

* Operating system with 1.8 compatible Java installed
* Curl or a similar tool to perform HTTP requests and read responses

### Running compiled and packaged application

* Start the application including simplified Tomcat server with ```java -jar message-server-1.0-SNAPSHOT.jar```
* Execute commands specifieds below

## Building

### Prerequisites for building

* Maven 3
* 1.8 compatible Java

### Building

* Clone GIT repository ```git clone git@github.com:natwie/JavaRestMessageServer.git```
* Download dependencies and create executable jar ```mvn clean package```

## HTTP requests supported examples

1) To create a new message from a sender to a recipient

```curl -X POST http://localhost:8080/api/v1.0/message/create/ -d "message={value}" -d "from={value}" -d "to={value}"```

2) To see and mark all messages as read

```curl -X PUT http://localhost:8080/api/v1.0/messages/```

3) To see and mark unread messages as read

```curl -X PUT http://localhost:8080/api/v1.0/messages/unread/```

4) To fetch messages in specific range, in time order, where start and end and int indexes

```curl -X PUT http://localhost:8080/api/v1.0/messages/range/{start}/{end}/```

5) To delete all messages

```curl -X DELETE http://localhost:8080/api/v1.0/messages/delete/```

6) To delete a specific messages, based on id, where id is an int

```curl -X DELETE http://localhost:8080/api/v1.0/messages/delete/{id}/```

7) To delete a range of messages

```curl -X DELETE http://localhost:8080/api/v1.0/messages/delete/inrange/{start}/{end}/```
