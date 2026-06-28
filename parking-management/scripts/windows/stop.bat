@echo off
set ROOT_DIR=%~dp0..\..

echo Encerrando sistema...
docker compose -f "%ROOT_DIR%\docker-compose.simulator.yml" down
docker compose -f "%ROOT_DIR%\docker-compose.yml" down -v
echo Sistema encerrado!
pause