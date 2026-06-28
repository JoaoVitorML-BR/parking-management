# Parking Management — Estapar Technical Challenge

Backend system for parking management: space control, vehicle entry/exit, and revenue calculation.

---

## Tecnologias

- **Java 21**
- **Spring Boot 3.5**
- **MySQL 8.0**
- **Docker & Docker Compose**
- **Swagger / OpenAPI 3**
- **JUnit 5 + Mockito**

---

## Arquitetura

The project follows a layered architecture with a clear separation of responsibilities:

```
src/main/java/com/estapar/challenge/parking_management/
├── config/          # Configurations (HTTP client, Swagger, Startup)
├── controller/      # Endpoints REST (Webhook, Revenue)
├── domain/service/  # Business rules (DynamicPricing, ParkingPricing)
├── dto/             # Data transfer objects
├── exception/       # Global error handling
├── models/          # JPA Entities (Garage, Spot, Ticket)
├── repository/      # Interfaces Spring Data JPA
├── service/         # Application services
└── usecase/         # Use cases (Entry, Parked, Exit, Revenue)
```

### Technical decisions

**Why two docker-compose files?**

The simulator (`cfontes0estapar/garage-sim:1.0.0`) has the webhook URL **hardcoded** as `http://localhost:3003/webhook` and ignores the `EXTERNAL_API_URL` environment variable. Therefore, it must run with direct access to the host network (`extra_hosts: localhost:host-gateway`), while the application and database reside on an isolated bridge network with standard port exposure.

- `docker-compose.yml` — application (Spring Boot) + database (MySQL)
- `docker-compose.simulator.yml` — simulator with host access

**Event flow:**

```
Simulator → POST /webhook (ENTRY)  → marks space as occupied + calculates dynamic factor
Simulator → POST /webhook (PARKED) → confirms physical position of the space
Simulator → POST /webhook (EXIT)   → frees up space + calculates final amount
```

**Dynamic pricing** (calculated and locked in at entry):
| Capacity       | Factor |
|---------------|-------|
| < 25%         | 0.90 (−10%) |
| 25% a 50%     | 1.00 (unchanged) |
| 50% a 75%     | 1.10 (+10%) |
| 75% a 100%    | 1.25 (+25%) |

**Calculation of the value (at EXIT):**
- First 30 minutes: **free**
- After 30 minutes: `basePrice × dynamicFactor × hours` (rounded up)
---

## Pré-requisitos

- [Docker](https://www.docker.com/) installed and running
- [Docker Compose](https://docs.docker.com/compose/) v2+
- Ports `3003` e `3306` available

---

## How to run it

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

---

## Endpoints

### Webhook — receives events from the simulator

**POST** `http://localhost:3003/webhook`

**ENTRY** — vehicle entrance:
```json
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```

**PARKED** — spot confirmation:
```json
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

**EXIT** — vehicle exit:
```json
{
  "license_plate": "ZUL0001",
  "exit_time": "2025-01-01T13:30:00.000Z",
  "event_type": "EXIT"
}
```

All of them return `HTTP 200` on success.

---

### Revenue — billing inquiry

**GET** `http://localhost:3003/revenue`

Request body:
```json
{
  "date": "2025-01-01",
  "sector": "A"
}
```

Response:
```json
{
  "amount": 151.88,
  "currency": "BRL",
  "timestamp": "2025-01-01T13:30:00.000Z"
}
```

> **Note on Swagger:** Browsers do not support GET requests with a body. Use **Insomnia**, **Postman**, or **curl** to test the `/revenue` endpoint. The other endpoints work normally in Swagger.

---

## Documentation Swagger

After starting up the system, access:

```
http://localhost:3003/api-doc
```

---

## Tests

```bash
./mvnw test
```

Unit test coverage:

| Class | Scenarios |
|--------|----------|
| `DynamicPricingServiceTest` | 4 occupancy tiers + zero capacity |
| `ParkingPricingServiceTest` | 30 min free, 31 min, 3 hours, discount factor |
| `EntryUseCaseTest` | normal entry, full parking lot, missing `entry_time` |
| `ExitUseCaseTest` | calculation and release, ticket not found, missing `exit_time` |
| `RevenueUseCaseTest` | total sum, no tickets, tickets with null amount |
---

## Project structure

```
parking-management/
├── scripts/
│   ├── linux/
│   │   ├── start.sh
│   │   └── stop.sh
│   └── windows/
│       ├── start.bat
│       └── stop.bat
├── src/
│   ├── main/
│   └── test/
├── docker-compose.yml           # App + database
├── docker-compose.simulator.yml # Simulator
├── Dockerfile
└── pom.xml
```

---

## Environment variables

Defined in `.env` and `docker-compose.yml`:

| Variable | Description | Default |
|----------|-----------|--------|
| `MYSQL_DATABASE` | Database name | `parking_management` |
| `MYSQL_PASSWORD` | MySQL password | `123456` |
| `SERVER_PORT` | Application port | `3003` |
| `GARAGE_SIMULATOR_BASE_URL` | Simulator base URL | `http://host.docker.internal:3000` |