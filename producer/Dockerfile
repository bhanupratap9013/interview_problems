# Build the project using Maven
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# Copy the project files into the container
COPY . ./

# Build the JAR file using Maven
RUN mvn clean package

# Use a lighter image to run the built JAR
FROM openjdk:17
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /app/target/*.jar /app/event_publisher.jar

# Command to run the application
CMD ["java", "-jar", "/app/event_publisher.jar"]