@echo off
set ROOT_DIR=%~dp0..\..

echo Iniciando Parking Management...

echo Derrubando volumes para evitar problemas de cache...
docker compose -f "%ROOT_DIR%\docker-compose.yml" down -v

echo Subindo banco de dados e aplicacao...
docker compose -f "%ROOT_DIR%\docker-compose.yml" up -d --build

echo Aguardando aplicacao iniciar (60s)...
timeout /t 60 /nobreak

echo Subindo simulador...
docker compose -f "%ROOT_DIR%\docker-compose.simulator.yml" up -d

echo Sistema iniciado!
echo Para finalizar o sistema, execute o stop.bat
echo.
echo Endpoints disponiveis:
echo    Webhook:  http://localhost:3003/webhook
echo    Revenue:  http://localhost:3003/revenue
echo    Swagger:  http://localhost:3003/api-doc
pause