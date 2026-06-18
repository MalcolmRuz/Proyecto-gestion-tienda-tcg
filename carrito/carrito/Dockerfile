# Multi-stage build: compile and package
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

# Copy Maven wrapper and pom.xml only (for dependency caching)
COPY mvnw mvnw.cmd ./
COPY .mvn ./.mvn
COPY pom.xml ./

# Download dependencies (cached layer)
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests

# Runtime stage: run the application
FROM eclipse-temurin:17-jre-alpine

# Instalar curl para el healthcheck de Docker
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/target/carrito-0.0.1-SNAPSHOT.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
