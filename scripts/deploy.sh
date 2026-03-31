#!/bin/bash
set -e

echo "Starting deployment on EC2..."

echo "$GHCR_TOKEN" | docker login ghcr.io -u "$GHCR_USERNAME" --password-stdin

docker pull "$GHCR_IMAGE"

docker stop music-app-container || true
docker rm music-app-container || true

docker run -d \
  --name music-app-container \
  -p ${APP_PORT}:${APP_PORT} \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://${RDS_ENDPOINT}:3306/movie_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME="${DB_USERNAME}" \
  -e SPRING_DATASOURCE_PASSWORD="${DB_PASSWORD}" \
  -e SERVER_PORT="${APP_PORT}" \
  "$GHCR_IMAGE"

echo "Deployment finished."
