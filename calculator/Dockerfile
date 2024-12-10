# Use official OpenJDK image as base
FROM openjdk:21-jdk-slim AS builder

# Set the working directory in the container
WORKDIR /app

# Copy Gradle wrapper files and build files
COPY gradlew ./
COPY gradle/wrapper/ ./gradle/wrapper/
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src ./src

# Make the gradlew script executable
RUN chmod +x gradlew

# Build the project (skip tests for now)
RUN ./gradlew build -x test

# Create a new image to run the application
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR from the builder image
COPY --from=builder /app/build/libs/calculator-0.0.1-SNAPSHOT.jar ./calculator.jar

# Expose port 8081 for the application
EXPOSE 8081

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "calculator.jar"]
