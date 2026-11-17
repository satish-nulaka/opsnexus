# TaskFlow API - Enterprise Java DevOps Pipeline
## 7-Stage End-to-End CI/CD Pipeline with Jenkins, Docker, and Prometheus

This project provides an enterprise-ready DevOps implementation for **TaskFlow API**, a Java 17 / Spring Boot 3 REST microservice with automated continuous integration and continuous deployment via Jenkins.

---

## Architecture & Tech Stack

* **Language & Framework:** Java 17 LTS, Spring Boot 3.2.3, Spring Security, Spring Boot Actuator
* **Build Tool:** Apache Maven 3.9
* **Testing:** JUnit 5 (Jupiter), AssertJ, MockMvc, JaCoCo Code Coverage
* **Code Quality:** SonarQube Scanner (`sonar-maven-plugin`), Checkstyle rules
* **Security:** OWASP Dependency Analysis, Trivy container scanner, Secret auditing
* **Containers & Orchestration:** Docker (Multi-stage build), Docker Compose
* **Monitoring & Observability:** Micrometer Prometheus exporter (`/actuator/prometheus`), Prometheus server, Grafana

---

## The 7 Pipeline Stages

1. **Stage 1: Build & Package:** Compiles Java code with Maven, packages the standalone executable JAR, and creates tagged Docker images.
2. **Stage 2: Automated Testing:** Runs unit and integration test suites, records Surefire JUnit XML reports, and enforces JaCoCo coverage thresholds.
3. **Stage 3: Code Quality Analysis:** Runs SonarQube static analysis to check code maintainability, technical debt, and duplication.
4. **Stage 4: Automated Security Scanning:** Performs CVE scanning on third-party Maven dependencies and container base images using Trivy.
5. **Stage 5: Deploy to Staging:** Deploys containerized staging environment on port `8081` using Docker Compose and executes automated smoke tests.
6. **Stage 6: Release to Production:** Promotes release to production on port `8080` with resource constraints, health verification, and automated rollback recovery.
7. **Stage 7: Monitoring & Alerting:** Scrapes JVM and application metrics via Prometheus, checks alert thresholds, and validates incident simulation via `/api/chaos/error`.

---

## Local Development & Testing

### Running with Maven
```bash
# 1. Compile and package the JAR
JAVA_HOME=/opt/homebrew/opt/openjdk@17 mvn clean package

# 2. Run automated tests with JaCoCo coverage
JAVA_HOME=/opt/homebrew/opt/openjdk@17 mvn test

# 3. Launch application locally
JAVA_HOME=/opt/homebrew/opt/openjdk@17 java -jar target/taskflow-api-1.0.0.jar
```

### Running with Docker Compose
```bash
# Launch Jenkins & SonarQube
docker compose -f docker-compose.jenkins.yml up -d

# Launch Prometheus & Grafana
docker compose -f docker-compose.monitoring.yml up -d

# Launch Staging Environment (Port 8081)
IMAGE_TAG=staging docker compose -f docker-compose.staging.yml up -d

# Launch Production Environment (Port 8080)
IMAGE_TAG=latest docker compose -f docker-compose.prod.yml up -d
```

---

## Key Endpoints

| Resource | URL | Description |
| :--- | :--- | :--- |
| **Health Probe** | `http://localhost:8080/actuator/health` | Service liveness and readiness probe |
| **Prometheus Metrics**| `http://localhost:8080/actuator/prometheus` | JVM & application metrics stream |
| **User Login** | `POST http://localhost:8080/api/auth/login` | Returns JWT Bearer token |
| **Task Management** | `GET/POST http://localhost:8080/api/tasks` | Authenticated CRUD endpoints |
| **Chaos Endpoint** | `GET http://localhost:8080/api/chaos/error` | Incident simulation for alert validation |
