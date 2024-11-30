# Use the official OpenJDK 21 base image
FROM openjdk:21-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY target/CareFlow_Backend-0.0.1-SNAPSHOT.jar app.jar

# Copy the certificates directory if not bundled into the JAR
COPY src/main/resources/certs certs/

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
