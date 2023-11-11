FROM amazoncorretto:17-alpine
WORKDIR /app
ADD target/central-warehouse-service-0.0.1-SNAPSHOT.jar central-warehouse-service.jar
ENTRYPOINT ["java","-jar","central-warehouse-service.jar"]