# Multi-stage production Dockerfile for OpsNexus API (Java 23)
# Stage 1: Build & Package with Maven on Java 23
FROM maven:3.9.9-eclipse-temurin-23-alpine AS builder

WORKDIR /usr/src/app

COPY pom.xml ./
RUN mvn dependency:go-offline -B || true

COPY src/ ./src/
# Run automated tests during container build
RUN mvn test -B
# Package executable JAR
RUN mvn clean package -DskipTests=true -B

# Stage 2: Minimal, secure JRE 23 Production Image
FROM eclipse-temurin:23-jre-alpine AS production

LABEL maintainer="OpsNexus Engineering Team"
LABEL description="OpsNexus Enterprise Workflow API"
LABEL version="1.0.0"

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=production

WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

COPY --from=builder --chown=appuser:appgroup /usr/src/app/target/opsnexus-api-1.0.0.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
