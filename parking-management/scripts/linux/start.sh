#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo "Iniciando Parking Management..."

echo "Derrubando volumes para evitar problemas de cache..."
docker compose -f "$ROOT_DIR/docker-compose.yml" down -v

echo "Subindo banco de dados e aplicação..."
docker compose -f "$ROOT_DIR/docker-compose.yml" up -d --build

echo "Aguardando aplicação ficar pronta..."
until docker logs estapar-backend 2>&1 | grep -q "Started ParkingManagementApplication"; do
    echo "   App ainda não está pronto, aguardando..."
    sleep 10
done

echo "Subindo simulador..."
docker compose -f "$ROOT_DIR/docker-compose.simulator.yml" up -d

echo "Sistema iniciado com sucesso!"
echo ""
echo "   Endpoints disponíveis:"
echo "   Webhook:  http://localhost:3003/webhook"
echo "   Revenue:  http://localhost:3003/revenue"
echo "   Swagger:  http://localhost:3003/api-doc"