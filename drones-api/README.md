# Drone Dispatcher

### Documentation

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering
small loads. For our use case **the load is medications**.

A **Drone** has:

- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has:

- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).

### Requisites

* Java 17 installed
* Maven installed

### Database

An H2 in memory database was used, to specify or change connection values please do at the file placed
in `resources\application.properties` in both packages `java` and `test`
of the project.

> spring.datasource.url=jdbc:h2:mem:dronesdb
> spring.datasource.driverClassName=org.h2.Driver 
> spring.datasource.username=sa
> spring.datasource.password=password
> spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
> spring.sql.init.mode=always 
> spring.jpa.hibernate.ddl-auto=update

### Build and Run Project

Execute the following command for putting up and running REST API.

> .\mvnw spring-boot:run

### Run Integration Tests

Unit tests were created using JUnit 5.  
To execute them type following command.
> .\mvnw test


### API Documentation

API was documented using Swagger library. At the following url you may see the information after project is running:

> http://localhost:8080/drones-api/swagger-ui/index.html#/
