# Challenge

## :computer: How to execute

### 1. Run infrastructure with docker-compose
```bash
docker-compose up -d
```
### 2. Run payments spring-boot project
```bash
cd payments-processor
./mvnw -f ./launcher/ spring-boot:run -D "spring-boot.run.arguments=--spring.datasource.password=test"
```
### 3. Open Browser and navigate to 
```text
http://localhost:9000/start
```

## :memo: Notes

_Some notes or explaination of your solution..._

## :pushpin: Things to improve

_If u have more time or want to improve somthing..._
