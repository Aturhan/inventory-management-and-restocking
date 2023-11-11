FROM amazoncorretto:17-alpine AS builder
WORKDIR /app
ADD target/warehouse2-service-0.0.1-SNAPSHOT.jar warehouse2-service-0.0.1.jar
ENTRYPOINT ["java","-jar","warehouse2-service-0.0.1.jar"]