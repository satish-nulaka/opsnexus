# Java DevOps Pipeline Demo Video Presentation Script
## Target Duration: 7 to 8 Minutes (Maximum: 10 Minutes)

Use this step-by-step presentation outline to record your demo video.

---

### Timing Breakdown
* **0:00 - 1:00:** Introduction, Architecture & Git Repository
* **1:00 - 2:15:** Jenkins Pipeline Configuration (SCM & Declarative Jenkinsfile)
* **2:15 - 5:15:** Pipeline Execution across all 7 Stages
* **5:15 - 6:45:** Staging vs. Production Deployment Verification
* **6:45 - 8:00:** Prometheus Metrics, Alert Rules & Chaos Incident Simulation
* **8:00 - 8:30:** Conclusion

---

### Step 1: Introduction (0:00 - 1:00)
* **On Screen:** Terminal showing the project directory and the GitHub repository page.
* **Talking Points:**
  * Introduce your project: **TaskFlow API**, an enterprise Java 17 and Spring Boot 3 REST microservice.
  * Highlight the technology stack: Apache Maven, Spring Security with JWT, JUnit 5, Docker, Docker Compose, SonarQube, and Prometheus.
  * Point out the key project files: `pom.xml`, multi-stage `Dockerfile`, and the declarative `Jenkinsfile`.

### Step 2: Jenkins Configuration (1:00 - 2:15)
* **On Screen:** Jenkins UI at `http://localhost:8080`.
* **Talking Points:**
  * Navigate to the Pipeline job (`taskflow-pipeline`).
  * Open **Configure** and show the Pipeline section.
  * Demonstrate that the pipeline definition uses **Pipeline script from SCM**, pulling the `Jenkinsfile` directly from the Git repository.

### Step 3: 7-Stage Pipeline Run (2:15 - 5:15)
* **On Screen:** Click **Build Now** and view the Stage View or Blue Ocean console output.
* **Talking Points:**
  * **Stage 1 (Build & Package):** Maven compiles the Java code, packages the executable JAR file, and builds a version-tagged Docker image (`taskflow-api:v1.0.X`).
  * **Stage 2 (Automated Testing):** JUnit 5 executes unit tests (cryptographic functions) and MockMvc integration tests (CRUD lifecycle). Surefire reports are saved, and JaCoCo evaluates coverage.
  * **Stage 3 (Code Quality):** SonarQube scans the code structure, maintainability, and code smells against defined quality gates.
  * **Stage 4 (Security Scanning):** Dependency trees and container layers are scanned for CVEs using Trivy, and secrets scanning checks for credential leaks.
  * **Stage 5 (Deploy to Staging):** Docker Compose deploys the container to staging on port `8081`. An automated script checks `/actuator/health` to confirm the service is UP.
  * **Stage 6 (Release to Production):** The verified image is promoted to production on port `8080` with resource constraints. Automated smoke tests verify health, with automatic rollback triggered if failures occur.
  * **Stage 7 (Monitoring & Alerting):** Verifies that Prometheus is scraping `/actuator/prometheus` and validates alerting rules via the chaos endpoint.

### Step 4: Verification of Deployments (5:15 - 6:45)
* **On Screen:** Web browser tabs.
* **Talking Points:**
  * Open `http://localhost:8081/actuator/health` (Staging) -> show `{"status":"UP"}`.
  * Open `http://localhost:8080/actuator/health` (Production) -> show `{"status":"UP"}`.
  * Use curl or Postman to send a POST request to `/api/auth/login` to obtain a JWT token, then fetch tasks from `http://localhost:8080/api/tasks` using the Bearer token.

### Step 5: Monitoring & Chaos Alerting (6:45 - 8:00)
* **On Screen:** Prometheus (`http://localhost:9090`) and Grafana (`http://localhost:3005`).
* **Talking Points:**
  * In Prometheus, search for `jvm_memory_used_bytes` or `taskflow_task_operations_total` to show live metrics.
  * Navigate to **Alerts** in Prometheus to show the `HighErrorRate` and `ServiceDown` rules.
  * Trigger an incident by navigating to `http://localhost:8080/api/chaos/error?type=500`.
  * Return to Prometheus to show how the error rate metric increases and triggers the alerting rule.

### Step 6: Conclusion (8:00 - 8:30)
* **On Screen:** Jenkins Stage View with all 7 stages completed in green.
* **Talking Points:**
  * Summarize the end-to-end automation: code compilation, multi-layer testing, quality gating, security analysis, staging validation, resilient production release, and live observability.
