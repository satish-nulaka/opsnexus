#!/usr/bin/env bash
set -e

TARGET_URL=${1:-"http://host.docker.internal:8080"}
FALLBACK_URL=${2:-""}
MAX_ATTEMPTS=20
SLEEP_INTERVAL=3

echo "=========================================================="
echo " [SMOKE TEST] Verifying Java Spring Boot Health"
echo " Primary Target:   ${TARGET_URL}"
if [ -n "${FALLBACK_URL}" ]; then
  echo " Fallback Target:  ${FALLBACK_URL}"
fi
echo "=========================================================="

ATTEMPT=1
TEST_URL="${TARGET_URL}"

while true; do
  if curl -s -f "${TARGET_URL}/actuator/health" > /dev/null 2>&1; then
    TEST_URL="${TARGET_URL}"
    break
  fi
  
  if [ -n "${FALLBACK_URL}" ]; then
    if curl -s -f "${FALLBACK_URL}/actuator/health" > /dev/null 2>&1; then
      TEST_URL="${FALLBACK_URL}"
      break
    fi
  fi
  
  if [ $ATTEMPT -ge $MAX_ATTEMPTS ]; then
    echo "ERROR: Health probe FAILED after ${MAX_ATTEMPTS} attempts on ${TARGET_URL}!"
    exit 1
  fi
  
  echo "Attempt ${ATTEMPT}/${MAX_ATTEMPTS}: Service warming up. Retrying in ${SLEEP_INTERVAL}s..."
  sleep $SLEEP_INTERVAL
  ATTEMPT=$((ATTEMPT + 1))
done

HEALTH_RESPONSE=$(curl -s "${TEST_URL}/actuator/health")
echo "SUCCESS: Spring Boot actuator reported UP on ${TEST_URL}!"
echo "Payload: ${HEALTH_RESPONSE}"

echo "Verifying /actuator/prometheus metrics stream..."
curl -s -f "${TEST_URL}/actuator/prometheus" | grep -E "jvm_memory|opsnexus_" | head -n 10
echo ""
echo "Smoke test passed successfully."
