## How to run it

> All commands below must be run from the **root of the project** (`parking-management/`).

### Linux / WSL (recommended)

```bash
# Grant execution permission (only the first time)
chmod +x scripts/linux/start.sh scripts/linux/stop.sh

# Start the system
./scripts/linux/start.sh

# Stop the system
./scripts/linux/stop.sh
```

### Windows

```bat
# Start the system
scripts\windows\start.bat

# Stop the system
scripts\windows\stop.bat
```

> **Windows without WSL:** the script waits a fixed 60 seconds for the app to start. If necessary, increase the `timeout` value in `scripts/windows/start.bat`.

### Manual (step-by-step)

```bash
# 1. Start the database and application
docker compose -f docker-compose.yml up -d --build

# 2. Wait for the application to start (check the logs)
docker logs -f estapar-backend

# 3. Once "Started ParkingManagementApplication" appears, start the simulator
docker compose -f docker-compose.simulator.yml up -d
```

## Documentation Swagger

After starting up the system, access:

```
http://localhost:3003/api-doc
```