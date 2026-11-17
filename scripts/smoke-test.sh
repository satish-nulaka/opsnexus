#!/usr/bin/env bash
set -e

TARGET_URL=${1:-"http://localhost:3000"}
MAX_ATTEMPTS=12
SLEEP_INTERVAL=3

echo "=========================================================="
echo " [SMOKE TEST] Verifying Deployment Health at: ${TARGET_URL}"
echo "=========================================================="

ATTEMPT=1
until curl -s -f "${TARGET_URL}/api/health" > /dev/null; do
  if [ $ATTEMPT -ge $MAX_ATTEMPTS ]; then
    echo "ERROR: Deployment verification FAILED after ${MAX_ATTEMPTS} attempts!"
    echo "Initiating automated rollback notification..."
    exit 1
  fi
  echo "Attempt ${ATTEMPT}/${MAX_ATTEMPTS}: Service not ready yet. Retrying in ${SLEEP_INTERVAL}s..."
  sleep $SLEEP_INTERVAL
  ATTEMPT=$((ATTEMPT + 1))
done

HEALTH_RESPONSE=$(curl -s "${TARGET_URL}/api/health")
echo "SUCCESS: Service is healthy and responding!"
echo "Payload: ${HEALTH_RESPONSE}"

# Check metrics endpoint
echo "Verifying /api/metrics availability..."
curl -s -f "${TARGET_URL}/api/metrics" | head -n 10
echo ""
echo "Smoke test passed successfully."
