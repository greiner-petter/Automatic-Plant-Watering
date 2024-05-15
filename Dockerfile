# Use the dependencies image as the base
FROM dependencies AS build

# Set working directory
WORKDIR /app

# Copy the application code
COPY . /app

# Build the project
RUN mvn clean package -Pproduction

# Create the final runtime image
FROM eclipse-temurin:21-jdk

# Copy the built jar from the build stage
COPY --from=build /app/target/iot-dashboard-1.0-SNAPSHOT.jar /app/iot-dashboard-1.0-SNAPSHOT.jar

# Expose port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "/app/iot-dashboard-1.0-SNAPSHOT.jar"]
