FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
WORKDIR /app/backend
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /target/backend-0-0.1-SNAPHOST.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","backend.jar"]