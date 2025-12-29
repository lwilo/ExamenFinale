# Quick Start Guide

## Prerequisites

Ensure you have the following installed:
- Docker & Docker Compose
- JDK 17+
- Maven 3.8+
- Node.js 18+ (for local frontend development)

## Deployment Options

### Option 1: Docker Compose (Recommended)

```bash
# Make deployment script executable
chmod +x deploy.sh

# Deploy entire system
./deploy.sh
```

Access services:
- Frontend: http://localhost:3000
- Eureka Dashboard: http://localhost:8761
- Axon Server: http://localhost:8124

### Option 2: Local Development

First, start infrastructure:
```bash
docker-compose up -d mysql axon-server zookeeper kafka keycloak
```

Then start services locally:
```bash
chmod +x start-local.sh stop-local.sh
./start-local.sh
```

Start frontend separately:
```bash
cd frontend
npm install
npm start
```

Stop services:
```bash
./stop-local.sh
```

## Testing the System

### Create a Keynote
```bash
curl -X POST http://localhost:8888/keynotes/commands \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com",
    "fonction": "Tech Lead"
  }'
```

### List Keynotes
```bash
curl http://localhost:8888/keynotes/queries
```

### Create a Conference
```bash
curl -X POST http://localhost:8888/conferences/commands \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Spring Boot Advanced",
    "type": "ACADEMIQUE",
    "date": "2024-06-15",
    "duree": 120,
    "nombreInscrits": 0
  }'
```

### List Conferences
```bash
curl http://localhost:8888/conferences/queries
```

### Add a Review
```bash
curl -X POST http://localhost:8888/conferences/commands/{conferenceId}/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "texte": "Excellente présentation!",
    "note": 5
  }'
```

## Troubleshooting

### Services not starting
Check logs:
```bash
# Docker
docker-compose logs [service-name]

# Local
tail -f logs/[service-name].log
```

### Port conflicts
Make sure ports 3000, 8080-8085, 8124, 8761, 8888, 9092, 9999 are available.

### Database connection errors
Wait for MySQL to fully start (may take 1-2 minutes).

## Architecture Overview

The system consists of:
- **9 microservices** (Discovery, Gateway, Auth, Keynote Command/Query, Conference Command/Query, Analytics)
- **Event Store** (Axon Server)
- **Message Broker** (Kafka)
- **Database** (MySQL)
- **Auth Server** (Keycloak)
- **Frontend** (React)

All services follow CQRS and Event Sourcing patterns.

## Next Steps

1. Explore the frontend at http://localhost:3000
2. Check service registry at http://localhost:8761
3. View event store at http://localhost:8124
4. Read detailed documentation in ARCHITECTURE.md and CLASS_DIAGRAM.md
