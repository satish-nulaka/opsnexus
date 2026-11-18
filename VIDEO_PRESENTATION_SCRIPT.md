# OpsNexus DevOps Pipeline Demo Video Presentation Script
## Target Duration: 7 to 8 Minutes (Strict Maximum: 10 Minutes)

This script provides an exact timestamp-by-timestamp walkthrough for recording your presentation video to demonstrate the completed 7-stage CI/CD pipeline.

---

### Presentation Timing Overview
* **0:00 - 1:00:** Introduction & System Architecture (GitHub repository & project structure)
* **1:00 - 2:00:** Jenkins Pipeline Configuration (Pipeline script from SCM)
* **2:00 - 4:45:** Live Jenkins Pipeline Execution (Stages 1 through 7)
* **4:45 - 6:00:** Staging vs. Production Deployment & Swagger UI Verification
* **6:00 - 7:15:** Prometheus Metrics, Alert Rules & Chaos Error Simulation
* **7:15 - 8:00:** Summary & Conclusion

---

### Step 1: Introduction & Architecture (0:00 - 1:00)
* **On Screen:** Open the GitHub repository at `https://github.com/satish-nulaka/opsnexus` and show your local project directory.
* **What to Say:**
  > *"Hello everyone. Today I am presenting my enterprise DevOps CI/CD pipeline implementation for **OpsNexus API**, a resilient Java 23 and Spring Boot 3.3.4 REST microservice. The solution automates all seven distinct lifecycle stages using Jenkins, Docker multi-stage builds, Trivy security scanning, Prometheus observability, and Grafana dashboards. Here on GitHub, we have our source repository containing the Java application code, pom.xml, multi-stage Dockerfile, and the declarative Jenkinsfile."*

---

### Step 2: Jenkins Pipeline Setup (1:00 - 2:00)
* **On Screen:** Jenkins UI at `http://localhost:8085`. Navigate to the `opsnexus-pipeline` job and click **Configure**.
* **What to Say:**
  > *"Here on our local Jenkins server running on port 8085, we have configured our pipeline job named `opsnexus-pipeline`. Under the Pipeline definition section, you can see it is configured with **Pipeline script from SCM**, pointing directly to our Git repository on GitHub on branch `main`. This ensures complete GitOps traceability: every pipeline execution is version-controlled and immutable."*

---

### Step 3: Live 7-Stage Pipeline Execution (2:00 - 4:45)
* **On Screen:** Click **Build Now** in Jenkins and click on the running build to show the Stage View / Console Output.
* **What to Say as each stage executes:**
  * **Stage 1 (Build & Package):** *"Stage 1 uses a multi-stage Docker build with Eclipse Temurin 23 and Maven 3.9 to compile our Java 23 code, run unit tests, and package our standalone JAR artifact into a secured container image tagged with the build number."*
  * **Stage 2 (Automated Testing):** *"Stage 2 executes our JUnit 5 unit and integration test suite, validating our cryptographic JWT token service and CRUD controllers, producing Surefire test reports."*
  * **Stage 3 (Code Quality Analysis):** *"Stage 3 triggers our SonarQube quality gate analysis, inspecting cyclomatic complexity, code smells, and technical debt against defined standards."*
  * **Stage 4 (Security Scanning):** *"Stage 4 runs our DevSecOps security audit using Trivy to scan the container base layers and Maven dependency tree for CVEs, as well as checking for any exposed secrets in the codebase."*
  * **Stage 5 (Deploy to Staging):** *"Stage 5 deploys the container to our Staging environment on port 8081 and executes our automated smoke test script, verifying that the Spring Boot health probe reports UP."*
  * **Stage 6 (Release to Production):** *"Stage 6 promotes our verified artifact to Production on port 8080 with automated health verification and automated rollback recovery if failure occurs."*
  * **Stage 7 (Monitoring & Alerting):** *"Stage 7 confirms our Prometheus metrics stream is active and triggers our chaos endpoint to validate error alerting."*
  * *"As you can see, all seven stages have completed with green success status!"*

---

### Step 4: Staging & Production Deployment Verification (4:45 - 6:00)
* **On Screen:** Open browser tabs for Staging and Production endpoints.
* **What to Show & Say:**
  1. **Staging Health:** Open `http://localhost:8081/actuator/health`.
     > *"Here on port 8081, our staging environment is actively running and returning health status UP."*
  2. **Production Swagger UI:** Open `http://localhost:8080/swagger-ui.html`.
     > *"Here on port 8080, our production environment is running with interactive Swagger UI. Let's authenticate using `POST /api/auth/login` with username `admin` and password `password123`. We obtain our JWT bearer token, click Authorize in Swagger UI, and execute `GET /api/tasks` and `POST /api/tasks` to demonstrate full authenticated CRUD functionality."*

---

### Step 5: Observability & Chaos Incident Simulation (6:00 - 7:15)
* **On Screen:** Prometheus at `http://localhost:9090` and Grafana at `http://localhost:3005`.
* **What to Show & Say:**
  1. **Prometheus Targets:** In Prometheus, open **Status -> Targets**.
     > *"In Prometheus, we can see both our staging and production targets are marked UP, actively scraping JVM and HTTP telemetry."*
  2. **Alert Rules:** Open **Alerts** tab in Prometheus.
     > *"Under Alerts, we have configured `ServiceDown` and `HighErrorRate` rules to safeguard our microservice."*
  3. **Chaos Simulation:** Open a browser tab or run curl to hit `http://localhost:8080/api/chaos/error?type=500`.
     > *"Let's simulate a production incident by triggering our chaos endpoint on port 8080. Returning to Prometheus and Grafana, we can immediately observe the HTTP 500 error count and rate spike, proving our real-time monitoring and alerting pipeline."*

---

### Step 6: Summary & Conclusion (7:15 - 8:00)
* **On Screen:** Return to Jenkins showing the all-green 7-Stage pipeline view.
* **What to Say:**
  > *"To summarize: we have built a complete, enterprise-grade CI/CD pipeline covering build automation, automated testing, quality gates, security vulnerability scanning, staging validation, resilient production deployment with rollback, and live Prometheus observability. Thank you for watching."*
