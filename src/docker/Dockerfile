# Use Maven image to build the project
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY ../pom.xml .
COPY ../src ./src

# Build the project
RUN mvn clean package -DskipTests

# Use JRE for runtime
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/incident-simulator-1.0-SNAPSHOT.jar app.jar

# Copy resources
COPY ../src/main/resources ./resources

# Create logs directory
RUN mkdir -p /app/logs

# Run the simulator
ENTRYPOINT ["java", "-jar", "app.jar"]
