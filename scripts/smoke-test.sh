#!/usr/bin/env bash
set -e

TARGET_URL=${1:-"http://localhost:8080"}
MAX_ATTEMPTS=15
SLEEP_INTERVAL=3

echo "=========================================================="
echo " [SMOKE TEST] Verifying Java Spring Boot Health: ${TARGET_URL}"
echo "=========================================================="

ATTEMPT=1
until curl -s -f "${TARGET_URL}/actuator/health" > /dev/null; do
  if [ $ATTEMPT -ge $MAX_ATTEMPTS ]; then
    echo "ERROR: Health probe FAILED after ${MAX_ATTEMPTS} attempts!"
    exit 1
  fi
  echo "Attempt ${ATTEMPT}/${MAX_ATTEMPTS}: Service warming up. Retrying in ${SLEEP_INTERVAL}s..."
  sleep $SLEEP_INTERVAL
  ATTEMPT=$((ATTEMPT + 1))
done

HEALTH_RESPONSE=$(curl -s "${TARGET_URL}/actuator/health")
echo "SUCCESS: Spring Boot actuator reported UP!"
echo "Payload: ${HEALTH_RESPONSE}"

echo "Verifying /actuator/prometheus metrics stream..."
curl -s -f "${TARGET_URL}/actuator/prometheus" | grep -E "jvm_memory|taskflow_" | head -n 10
echo ""
echo "Smoke test passed successfully."
