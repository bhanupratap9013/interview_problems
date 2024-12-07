# Step 1: Use Maven to build the project
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . ./
RUN mvn clean package

# Step 2: Create a lightweight image to run the JAR
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/consumer-1.0-SNAPSHOT.jar /app/event_consumer.jar

CMD ["java", "-jar", "/app/event_consumer.jar"]