#!/usr/bin/env bash

set -e

echo "Starting GestionDeProjets demo stack..."
docker-compose up -d --build

echo
echo "Demo stack started."
echo "Frontend:   http://localhost:4200"
echo "Backend:    http://localhost:8085/api"
echo "Swagger:    http://localhost:8085/api/swagger-ui.html"
echo "phpMyAdmin: http://localhost:8084"
echo "Prometheus: http://localhost:9090"
echo "Grafana:    http://localhost:3000"
echo
echo "Grafana credentials: admin / admin"
