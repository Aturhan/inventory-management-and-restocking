# Starting

It is a microservice project related to inventory management and restocking. There are 2 local warehouses and 1 central warehouse. According to the scenario, when the amount of products in local warehouses equals or falls below the threshold value, the central warehouse is notified via Kafka and the central warehouse takes action according to this event.

## Installation

- Clone the project's GitHub repository;
- mvn clean package

```

## Swagger 
-  Warehouse1-service: https://localhost:8081/swagger-ui.html
-  Warehouse1-service: https://localhost:8082/swagger-ui.html
- Central-service: https://localhost:8083/swagger-ui.html
```

## Dependencies
- Spring web
- Webflux
- Kafka
- Lombok
- Eureka
- Data JPA
- PostgreSQL



## License

[MIT](https://choosealicense.com/licenses/mit/)