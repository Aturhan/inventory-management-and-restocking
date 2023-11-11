FROM amazoncorretto:17-alpine AS builder
WORKDIR /app
ADD target/discovery-server-0.0.1-SNAPSHOT.jar discovery-server-0.0.1.jar
ENTRYPOINT ["java","-jar","discovery-server-0.0.1.jar"]