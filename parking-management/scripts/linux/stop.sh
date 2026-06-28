#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo "Encerrando sistema..."
docker compose -f "$ROOT_DIR/docker-compose.simulator.yml" down
docker compose -f "$ROOT_DIR/docker-compose.yml" down -v
echo "Sistema encerrado!"