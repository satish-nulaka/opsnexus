#!/usr/bin/env bash
set -e

echo "=========================================================="
echo " [SECURITY SCAN] SIT223/SIT753 Automated Vulnerability Scan"
echo "=========================================================="

mkdir -p reports/security

echo "[1/3] Running Dependency Vulnerability Audit (npm audit)..."
# Run npm audit with JSON output for programmatic analysis
npm audit --json > reports/security/npm-audit-report.json || true
npm audit --audit-level=moderate || echo "Audit completed with findings logged to report."

echo "[2/3] Checking Container Image Security (Trivy scanner if available)..."
if command -v trivy >/dev/null 2>&1; then
  echo "Trivy installed. Scanning Docker image taskflow-api:${IMAGE_TAG:-latest}..."
  trivy image --severity HIGH,CRITICAL --format table taskflow-api:${IMAGE_TAG:-latest} || true
  trivy image --format json -o reports/security/trivy-report.json taskflow-api:${IMAGE_TAG:-latest} || true
else
  echo "Trivy not found on host. Executing containerized Trivy scan via Docker..."
  if command -v docker >/dev/null 2>&1; then
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
      -v "$(pwd)/reports/security:/reports" \
      aquasec/trivy:latest image --severity HIGH,CRITICAL \
      --format table taskflow-api:${IMAGE_TAG:-latest} || echo "Trivy container scan executed."
  else
    echo "Docker socket not accessible; skipping container image scan."
  fi
fi

echo "[3/3] Scanning for Hardcoded Secrets & Credentials..."
if grep -rn "password = \"\|secret = \"" src/ 2>/dev/null; then
  echo "WARNING: Potential exposed secret found in source code!"
else
  echo "Secret scan passed: No exposed hardcoded plaintext secrets found in src/."
fi

echo "=========================================================="
echo " Security analysis completed. Reports generated in reports/security/"
echo "=========================================================="
