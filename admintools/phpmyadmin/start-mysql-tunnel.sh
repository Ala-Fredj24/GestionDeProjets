#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="${NAMESPACE:-gestiondeprojets}"
SERVICE="${SERVICE:-mysql}"
LOCAL_PORT="${LOCAL_PORT:-3307}"
REMOTE_PORT="${REMOTE_PORT:-3306}"

DOCKER_GATEWAY="${DOCKER_GATEWAY:-$(docker network inspect bridge --format '{{(index .IPAM.Config 0).Gateway}}' 2>/dev/null || true)}"
if [ -z "$DOCKER_GATEWAY" ]; then
  DOCKER_GATEWAY="172.17.0.1"
fi

echo "Forwarding ${NAMESPACE}/${SERVICE}:${REMOTE_PORT} to 127.0.0.1:${LOCAL_PORT} and ${DOCKER_GATEWAY}:${LOCAL_PORT}"
echo "Keep this terminal open while using phpMyAdmin."

exec kubectl port-forward \
  --address "127.0.0.1,${DOCKER_GATEWAY}" \
  "svc/${SERVICE}" \
  "${LOCAL_PORT}:${REMOTE_PORT}" \
  -n "${NAMESPACE}"
