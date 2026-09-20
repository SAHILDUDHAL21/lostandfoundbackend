# Stage 1: Build application using Maven and JDK 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create lightweight production JRE runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy compiled JAR artifact from builder stage
COPY --from=builder /app/target/lostandfound-0.0.1-SNAPSHOT.jar app.jar

# Set permissions
RUN chown -R appuser:appgroup /app
USER appuser

# Render injects PORT dynamically; 8080 is default fallback
EXPOSE 8080

# Execute Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

