# --- Etapa 1: Construcción (Multi-stage Build) ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src


RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN mkdir -p /app/oracle_wallet

COPY --from=build /app/target/*.jar app.jar
COPY ./oracle_wallet /app/oracle_wallet

# Lectura de puerto dinámico
EXPOSE ${PORT:-8081}

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Doracle.net.tns_admin=/app/oracle_wallet", "-jar", "app.jar"]