# Challenge

## :computer: How to execute

### 1. Run infrastructure with docker-compose
```bash
wf-delivery>  docker-compose up -d
```
### 2. Download dependencies, run tests, build modules and Run payments spring-boot project
```bash
wf-delivery>  cd payments-processor
wf-delivery/payments-processor> ./mvnw -U clean install
wf-delivery/payments-processor> ./mvnw -f ./launcher/ spring-boot:run -D "spring-boot.run.arguments=--spring.datasource.password=test"
```
### 3. Open Browser and navigate to 
```text
http://localhost:9000/start
```

This will start the server at localhost:9000
* [Logs](http://localhost:9000/logs) - view logs
* [Start](http://localhost:9000/start) - reset application
* [Zipkin](http://localhost:9411/) - for tracing

## :memo: Notes

### Tech Stack
The service is built using only Java with SpringBoot. Apart from the starter libraries provided by [SpringBoot Initializer](https://start.spring.io/),folowing dependencies have been used:
* [Lombok](https://projectlombok.org/) - to reduce boilerplate code.
* [Jakarta Bean Validation API](https://beanvalidation.org/) - to use NotNull for uuid's in model and response.
* [Zipkin](https://zipkin.io/) - distributed tracing system

I choose Java and SpringBoot due to following reasons:
#### Pros
* One of the best combinations in JVM ecosystems.
* Good support and integration with many technologies.
* Many developers, easy to maintain and refactor.
#### Cons
* Memory consumption is high.
* Startup times compared to nodejs and golang can be higher.

### Architecture Design
I choose a hexagonal architecture pattern to implement the solutions based on domain, infra, ports and adapters. The hexagonal architecture focuses on three principles: Explicitly separate user side, business logic, and server side layers.
it provides a good separation between domain, business logic and adapters.
<br>
WebClient from spring reactive has been used to rest communication as it provides of both Sync and Async feature with many benefits. 
Moreover, going further there will no further development in Spring RestTemplate.

#### Pros
* Each layer of the application has a strict set of responsibilities and concerns.
* Test business logic in isolation from external systems.
* Agnostic to the outside world.
* Independent of external services due to ports and adapters.
#### Cons
* Higher level of complexity compared to DDD model or MVC model.
* Adapters are traditionally polymorphic (in OO), so polymorphic calls can be harder to understand and debug.

### Observability
Observability refers to telemetry produced by services. There are three pillars for observability: logs, distributed traces and metrics. 
In microservices it largely revolves around making sure development teams have access to the data they need to identify problems and detect failures.

Zipkin and SpringSleuth has been chosen as it is open-source, integrate very well and easy to integrate in prototype or small scale microservice applications

### Code Structure
1. The code is organized into following main packages:
    - **application**: This module contains interfaces as port. It also contains the two use cases for Online and Offline payment processing.
    - **domain**: This module contains model, values objects and exception classes.
    - **infra**: This package contains the adapter for jpa, kafka and rest.
    - **launcher**: This module contains configurations and main class to launch application.

## :pushpin: Things to improve

* Use openapi to generate rest clients.
* Add integration tests using test containers for postgres.
* Add integration tests for kafka consumers.
* Version api.
* Configure logging levels for layers.
* Use a message broker for pub-sub communication pattern between microservice and log endpoint.
* Add more partitions to the payment topics to enable the parallel consuming.
* Consume Dead Letter Queue topics to process errors in payment.
