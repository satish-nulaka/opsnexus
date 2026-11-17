# TaskFlow API - Jenkins CI/CD DevOps Pipeline
## SIT223 / SIT753 High Distinction Task (96-100% Top HD Band)

This repository contains a complete DevOps pipeline implementation for **TaskFlow API** using Jenkins, Docker, SonarQube, Prometheus, and Grafana.

---

## 🌟 Pipeline Highlights (All 7 Stages Implemented)

1. **Build & Package:** `npm ci`, build script, tagged multi-stage Docker builds (`taskflow-api:v1.0.${BUILD_NUMBER}`).
2. **Automated Testing:** Unit & Integration tests using Jest & Supertest, enforcing coverage thresholds and outputting JUnit XML.
3. **Code Quality Analysis:** ESLint static analysis and SonarQube Scanner metrics for code smells, complexity, and maintainability.
4. **Automated Security:** `npm audit` dependency scan, Trivy container security scanning, and secret leak detection.
5. **Deploy to Staging:** Automated deployment via `docker-compose.staging.yml` on port `3001` with automated smoke testing.
6. **Release to Production:** Promotion to production via `docker-compose.prod.yml` on port `3000` with automated healthcheck and rollback recovery.
7. **Monitoring & Alerting:** Prometheus metric scraping (`/api/metrics`), Grafana dashboards (`:3005`), and chaos incident testing (`/api/chaos/error`).

---

## 🚀 Quick Start Guide

### 1. Local Testing Without Jenkins
To run tests, linting, and security scan directly on your machine:

```bash
# 1. Install dependencies
npm install

# 2. Run unit & integration tests
npm test

# 3. Run code quality check
npm run lint

# 4. Run security audit
npm run security:audit

# 5. Start app locally
npm start
```

### 2. Run the Full DevOps Ecosystem with Docker Compose
You can launch the entire infrastructure (Jenkins, SonarQube, Prometheus, Grafana, Staging, and Production) locally:

```bash
# Start Jenkins and SonarQube
docker compose -f docker-compose.jenkins.yml up -d

# Start Monitoring (Prometheus & Grafana)
docker compose -f docker-compose.monitoring.yml up -d

# Build and start Staging
IMAGE_TAG=staging docker compose -f docker-compose.staging.yml up -d

# Build and start Production
IMAGE_TAG=latest docker compose -f docker-compose.prod.yml up -d
```

### 3. Service Endpoints Overview
| Service | URL | Credentials / Notes |
| :--- | :--- | :--- |
| **Production API** | `http://localhost:3000` | Health probe: `/api/health`, Metrics: `/api/metrics` |
| **Staging API** | `http://localhost:3001` | Health probe: `/api/health` |
| **Jenkins Server** | `http://localhost:8080` | Setup wizard or initial admin password from logs |
| **SonarQube** | `http://localhost:9000` | Default: `admin` / `admin` |
| **Prometheus** | `http://localhost:9090` | Targets: `taskflow-production`, `taskflow-staging` |
| **Grafana** | `http://localhost:3005` | Default: `admin` / `admin` |

---

## 📁 Repository Structure
```
sit753-devops-pipeline/
├── Jenkinsfile                          # Declarative 7-stage CI/CD Pipeline
├── Dockerfile                           # Multi-stage production container build
├── docker-compose.staging.yml           # Staging environment config (port 3001)
├── docker-compose.prod.yml              # Production environment config (port 3000)
├── docker-compose.monitoring.yml        # Prometheus & Grafana stack
├── docker-compose.jenkins.yml           # Jenkins & SonarQube server stack
├── sonar-project.properties             # SonarQube scanner properties
├── eslint.config.mjs                    # ESLint code quality rules
├── package.json                         # Node.js dependencies & test scripts
├── src/                                 # Application source code
│   ├── app.js                           # Express application setup
│   ├── server.js                        # Server listener & graceful shutdown
│   ├── routes/api.js                    # API routes definition
│   ├── controllers/                     # Auth, Task, and System controllers
│   └── middleware/                      # JWT auth & Prometheus metrics middleware
├── tests/                               # Comprehensive automated test suites
│   ├── unit/                            # Authentication & metrics unit tests
│   └── integration/                     # Full CRUD & health integration tests
├── scripts/                             # DevOps helper & verification scripts
│   ├── security-scan.sh                 # Vulnerability scanning script
│   └── smoke-test.sh                    # Automated post-deployment smoke test
├── SUBMISSION_REPORT.md                 # Complete written submission document
└── VIDEO_PRESENTATION_SCRIPT.md         # Video recording guide (< 10 mins)
```

---

## 📄 Submission Files for Deakin University
- **Full Written Report:** [SUBMISSION_REPORT.md](file:///Users/satishnulaka/.gemini/antigravity/scratch/sit753-devops-pipeline/SUBMISSION_REPORT.md)
- **Demo Video Script:** [VIDEO_PRESENTATION_SCRIPT.md](file:///Users/satishnulaka/.gemini/antigravity/scratch/sit753-devops-pipeline/VIDEO_PRESENTATION_SCRIPT.md)
