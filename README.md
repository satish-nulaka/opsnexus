# OpsNexus API - Enterprise Java DevOps CI/CD Pipeline
## 7-Stage End-to-End Automated CI/CD Pipeline with Jenkins, Docker, Prometheus & Grafana

This repository delivers an enterprise-grade DevOps implementation for **OpsNexus API**, a modern **Java 23 / Spring Boot 3.3.4** REST microservice featuring full continuous integration and continuous deployment via Jenkins.

---

## Architecture & Technology Stack

* **Language & Runtime:** Java 23 (Eclipse Temurin 23)
* **Framework:** Spring Boot 3.3.4 (Spring Web, Spring Security, Spring Boot Actuator, Springdoc OpenAPI)
* **API Documentation:** Interactive OpenAPI / Swagger UI at `/swagger-ui.html`
* **Build System:** Apache Maven 3.9 (Containerized multi-stage Docker build)
* **Testing:** JUnit 5 (Jupiter), AssertJ, Spring MockMvc, JaCoCo Code Coverage
* **Code Quality:** SonarQube Scanner (`sonar-project.properties`)
* **Security & DevSecOps:** Trivy Container Image Scanner, Maven Dependency Tree Auditing, Secret Leakage Detection
* **Containers & Orchestration:** Docker multi-stage builds, Docker Compose, Linux non-root security (`appuser`)
* **Observability & Alerting:** Micrometer Prometheus exporter (`/actuator/prometheus`), Prometheus Server (`:9090`), Grafana Dashboard (`:3005`)
* **Chaos Engineering:** Synthetic failure injection endpoint (`/api/chaos/error`) for alert simulation

---

## The 7 Pipeline Stages (Jenkinsfile)

1. **Stage 1: Build & Package:** Compiles Java 23 code with Maven, runs unit tests, packages standalone executable JAR, and creates tagged Docker images (`opsnexus-api:latest`, `opsnexus-api:v1.0.${BUILD_NUMBER}`).
2. **Stage 2: Automated Testing:** Executes JUnit 5 test suites (cryptographic security tests, CRUD service tests), produces Surefire XML test reports, and enforces test passing criteria.
3. **Stage 3: Code Quality Analysis:** Runs SonarQube static analysis to evaluate code maintainability, technical debt, and quality gates.
4. **Stage 4: Automated Security Scanning:** Performs CVE analysis on container base images and dependencies using Trivy, audits secret exposure, and exports security artifacts.
5. **Stage 5: Deploy to Staging:** Deploys containerized staging environment on port `8081` and executes automated smoke tests with retry loops.
6. **Stage 6: Release to Production:** Promotes release to production on port `8080` with automated health verification and automated rollback recovery if failure occurs.
7. **Stage 7: Monitoring & Alerting:** Scrapes JVM and application metrics via Prometheus, triggers chaos endpoint (`/api/chaos/error?type=500`), and validates alerting rules.

---

## Service Endpoints & Verification

| Service / Tool | URL | Description |
| :--- | :--- | :--- |
| **Production Swagger UI** | `http://localhost:8080/swagger-ui.html` | Interactive API documentation & test console |
| **Production Health Probe** | `http://localhost:8080/actuator/health` | Service liveness and readiness probe |
| **Production Metrics** | `http://localhost:8080/actuator/prometheus` | Live Micrometer Prometheus JVM metric stream |
| **Staging Health Probe** | `http://localhost:8081/actuator/health` | Isolated pre-production verification endpoint |
| **Jenkins CI/CD Server** | `http://localhost:8085` | Declarative 7-stage pipeline orchestrator |
| **SonarQube Quality Server** | `http://localhost:9000` | Code quality, debt, and security scanner |
| **Prometheus Monitoring** | `http://localhost:9090` | Time-series metrics collection & alert rules |
| **Grafana Dashboards** | `http://localhost:3005` | JVM performance & traffic visual dashboard (`admin`/`admin`) |
| **Chaos Failure Injection** | `http://localhost:8080/api/chaos/error` | Synthetic error trigger for alert verification |

---

## Authentication & API Quickstart

### 1. Authenticate (Get JWT Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

### 2. Access Protected Task APIs
```bash
# Set your token
TOKEN="<JWT_TOKEN_FROM_STEP_1>"

# List all tasks
curl -X GET http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN"

# Create a new task
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Deploy Pipeline","description":"Automate 7 stages with Jenkins","status":"COMPLETED"}'
```

Or open **Swagger UI** at `http://localhost:8080/swagger-ui.html`, click **Authorize** 🔓, and enter `Bearer <your-token>`.
