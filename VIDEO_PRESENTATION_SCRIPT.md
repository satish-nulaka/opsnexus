# SIT223 / SIT753 DevOps Pipeline Demo Video Script
## 10-Minute Presentation Guide (Target Duration: ~7-8 Minutes)

This script gives you an exact, step-by-step walkthrough to record your submission video and secure the Top HD (96-100%) score.

---

### Timing Breakdown
| Time | Section | Screen Focus / Action |
| :--- | :--- | :--- |
| **0:00 - 1:00** | Introduction & Repository Setup | Terminal & GitHub Repo |
| **1:00 - 2:30** | Jenkins Pipeline Configuration | Jenkins Web UI |
| **2:30 - 5:30** | Pipeline Execution Through All 7 Stages | Jenkins Stage View & Console Logs |
| **5:30 - 7:00** | Live Deployment & Verification (Staging vs Prod) | Browser tabs (`:3001` and `:3000`) |
| **7:00 - 8:30** | Monitoring, Prometheus Metrics & Alert Simulation | Prometheus (`:9090`) & Grafana (`:3005`) |
| **8:30 - 9:00** | Summary & Conclusion | Jenkins summary view |

---

### Section 1: Introduction & Repository Setup (0:00 - 1:00)
**Action:**
1. Open your terminal and show the project folder.
2. Open your GitHub repository in your web browser.

**Voiceover Script:**
> *"Hello everyone and welcome to my submission for SIT223/SIT753: Professional Practice in IT. Today, I am demonstrating a complete, end-to-end DevOps CI/CD pipeline built with Jenkins for the Top High Distinction band.*
>
> *For this assessment, I built **TaskFlow API**—a Node.js and Express REST microservice that includes JWT authentication, full CRUD operations for tasks, Docker multi-stage containerization, and native Prometheus metrics. As you can see on GitHub, the repository contains the application source, comprehensive unit and integration tests, Docker Compose configurations, and our declarative `Jenkinsfile`."*

---

### Section 2: Jenkins Setup & Pipeline Configuration (1:00 - 2:30)
**Action:**
1. Switch to Jenkins at `http://localhost:8080`.
2. Click on the Pipeline job (`taskflow-pipeline`).
3. Click **Configure** and scroll to the **Pipeline** definition.
4. Show that it is configured to use **Pipeline script from SCM** pointing to your Git repository and `Jenkinsfile`.

**Voiceover Script:**
> *"Here in Jenkins, I have created a Pipeline job named `taskflow-pipeline`. Under configuration, the pipeline is set to fetch directly from our Git repository. This adheres to GitOps and Infrastructure-as-Code principles—any change committed to version control immediately defines how the pipeline executes.*
>
> *Now let's click 'Build Now' to trigger our pipeline and watch each stage execute."*

---

### Section 3: Walking Through the 7 Pipeline Stages (2:30 - 5:30)
**Action:**
1. Click **Build Now**.
2. Click on the active build number to view the console output or the blue ocean / stage view.
3. Walk through each stage as it turns green:

**Voiceover Script:**
> *"Let's examine the 7 stages as they execute:*
>
> 1. **Stage 1: Build & Package:** *The pipeline pulls down dependencies with `npm ci` to ensure strict repeatability, packages the codebase, and creates a tagged Docker image tagged with both semantic versioning and the current Git commit hash.*
>
> 2. **Stage 2: Automated Testing:** *Here, both unit tests and integration tests are executed using Jest and Supertest. Unit tests verify token generation and bcrypt hashing, while integration tests validate full CRUD endpoints. Results are automatically published to Jenkins in JUnit XML format.*
>
> 3. **Stage 3: Code Quality Analysis:** *Next, ESLint verifies JavaScript syntax, standards, and code hygiene, followed by SonarQube Scanner which inspects cyclomatic complexity, code smells, and maintainability quality gates.*
>
> 4. **Stage 4: Security Scanning:** *In our security stage, `npm audit` and Trivy scan third-party dependencies and container base images for CVEs. We also run secret detection to guarantee no sensitive API keys are exposed.*
>
> 5. **Stage 5: Deploy to Staging:** *Jenkins now uses Docker Compose to deploy our container to an isolated staging environment on port 3001. Notice the automated smoke test script that verifies the `/api/health` endpoint before allowing the pipeline to proceed.*
>
> 6. **Stage 6: Release to Production:** *With staging validated, the image is promoted to production on port 3000. Our pipeline includes automated health checks and an automated rollback mechanism that immediately reverts to the previous stable release if the new build fails smoke testing.*
>
> 7. **Stage 7: Monitoring & Alerting:** *Finally, the pipeline connects to our observability stack, validating that Prometheus is actively scraping `/api/metrics` and triggering an alert simulation via our chaos endpoint."*

---

### Section 4: Live Staging & Production Verification (5:30 - 7:00)
**Action:**
1. Open a new tab to `http://localhost:3001/api/health` (Staging).
2. Open a new tab to `http://localhost:3000/api/health` (Production).
3. In terminal, run a curl request to register and login a user or create a task.

**Voiceover Script:**
> *"Now let's verify both deployed environments live:*
>
> *Here on port 3001, we have our **Staging application**. The `/api/health` probe shows uptime, system memory, and environment set to 'staging'.*
>
> *Here on port 3000, we have our **Production application**. It shows the promoted production container running with production resource allocations.*
>
> *Let's test an authenticated request using curl against production. We pass our bearer token to `/api/tasks`, and we receive our task data immediately with HTTP 200 OK."*

---

### Section 5: Monitoring, Prometheus Metrics & Chaos Alerting (7:00 - 8:30)
**Action:**
1. Open `http://localhost:9090` (Prometheus UI).
2. Search for `taskflow_http_requests_total` and click **Execute**. Show the graph.
3. Click **Alerts** in the top navigation bar. Show `HighErrorRate` and `ServiceDown` rules.
4. Open `http://localhost:3000/api/chaos/error?type=500` in a tab to show an error being recorded.
5. In Prometheus, show how the error rate increases and the alert evaluates.
6. Open `http://localhost:3005` (Grafana) to show the dashboard.

**Voiceover Script:**
> *"Observability is critical in production. Here in Prometheus on port 9090, our application's `/api/metrics` endpoint is scraped every 5 seconds.*
>
> *Under 'Alerts', we have configured proactive rules: `ServiceDown` and `HighErrorRate`. To demonstrate our incident alerting in action, let's trigger our chaos endpoint at `/api/chaos/error`. As you can see, when 500 errors occur, Prometheus immediately registers the metric spike and transitions the alert state.*
>
> *Over in Grafana on port 3005, we have real-time dashboards visualizing request latency, throughput, and error rates."*

---

### Section 6: Conclusion (8:30 - 9:00)
**Action:**
1. Return to the Jenkins main page showing the completed, all-green build with 7 stages.

**Voiceover Script:**
> *"In summary, we have implemented all 7 stages required for the High Distinction band: Build, Test, Code Quality, Security, Staging Deploy, Production Release with rollback, and real-time Monitoring & Alerting.*
>
> *All source code, Docker configs, and reports are available in the GitHub repository linked in my submission document. Thank you for watching!"*
