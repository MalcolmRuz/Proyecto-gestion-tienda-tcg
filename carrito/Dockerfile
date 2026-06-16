# Multi-stage build: compile and package
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build
COPY . .
RUN ./mvnw clean package -DskipTests

# Runtime stage: run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/carrito-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
