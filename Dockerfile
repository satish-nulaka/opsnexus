# Multi-stage production-grade Dockerfile for Java Spring Boot TaskFlow API
# Stage 1: Build & Package with Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /usr/src/app

COPY pom.xml ./
# Cache Maven dependencies
RUN mvn dependency:go-offline -B || true

COPY src/ ./src/
RUN mvn clean package -DskipTests=true -B

# Stage 2: Minimal, secure JRE 17 Production Image
FROM eclipse-temurin:17-jre-alpine AS production

LABEL maintainer="TaskFlow DevOps Engineering Team"
LABEL description="TaskFlow API Java Spring Boot Microservice"
LABEL version="1.0.0"

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=production

WORKDIR /app

# Install curl for container health check
RUN apk add --no-cache curl

# Run as non-privileged user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Copy built JAR artifact from builder stage
COPY --from=builder --chown=appuser:appgroup /usr/src/app/target/taskflow-api-1.0.0.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
