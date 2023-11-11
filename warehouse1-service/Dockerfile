FROM amazoncorretto:17-alpine AS builder
WORKDIR /app
ADD target/warehouse1-service-0.0.1-SNAPSHOT.jar warehouse1-service.jar
ENTRYPOINT ["java","-jar","warehouse1-service.jar"]