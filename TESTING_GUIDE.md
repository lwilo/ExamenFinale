# Testing Guide

## Test Environment Setup

Before running tests, ensure:
1. All infrastructure services are running (MySQL, Axon Server, Kafka, Keycloak)
2. Discovery Service is running and accessible
3. Services are registered in Eureka

## Unit Tests

Each service contains unit tests for:
- Command handlers
- Event handlers
- Query handlers
- Business logic

Run unit tests:
```bash
# All services
mvn test

# Specific service
cd keynote-service-command
mvn test
```

## Integration Tests

### Test Infrastructure

Start test infrastructure:
```bash
docker-compose up -d mysql axon-server kafka
```

### API Integration Tests

#### 1. Test Keynote Service

**Create Keynote:**
```bash
curl -X POST http://localhost:8888/keynotes/commands \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Martin",
    "prenom": "Robert",
    "email": "robert.martin@example.com",
    "fonction": "Software Architect"
  }'
```

Expected Response: 201 Created with keynote ID

**Get All Keynotes:**
```bash
curl http://localhost:8888/keynotes/queries
```

Expected Response: 200 OK with array of keynotes

**Get Keynote by ID:**
```bash
curl http://localhost:8888/keynotes/queries/{keynote-id}
```

Expected Response: 200 OK with keynote details

**Update Keynote:**
```bash
curl -X PUT http://localhost:8888/keynotes/commands/{keynote-id} \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Martin",
    "prenom": "Robert C.",
    "email": "robert.martin@example.com",
    "fonction": "Clean Code Author"
  }'
```

Expected Response: 200 OK

**Delete Keynote:**
```bash
curl -X DELETE http://localhost:8888/keynotes/commands/{keynote-id}
```

Expected Response: 204 No Content

#### 2. Test Conference Service

**Create Conference:**
```bash
curl -X POST http://localhost:8888/conferences/commands \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Microservices Architecture Patterns",
    "type": "ACADEMIQUE",
    "date": "2024-07-20",
    "duree": 180,
    "nombreInscrits": 0
  }'
```

Expected Response: 201 Created with conference ID

**Get All Conferences:**
```bash
curl http://localhost:8888/conferences/queries
```

Expected Response: 200 OK with array of conferences

**Add Review to Conference:**
```bash
curl -X POST http://localhost:8888/conferences/commands/{conference-id}/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "texte": "Excellent content, very informative!",
    "note": 5
  }'
```

Expected Response: 201 Created

**Get Reviews for Conference:**
```bash
curl http://localhost:8888/conferences/queries/{conference-id}/reviews
```

Expected Response: 200 OK with array of reviews

#### 3. Test Analytics Service

**Get Real-Time Analytics:**
```bash
curl http://localhost:8888/analytics/reviews/realtime
```

Expected Response: 200 OK with analytics data

## Event Sourcing Tests

### Verify Event Store

1. Access Axon Server Dashboard: http://localhost:8124
2. Navigate to "Search Events"
3. Verify events are stored:
   - KeynoteCreatedEvent
   - KeynoteUpdatedEvent
   - ConferenceCreatedEvent
   - ReviewAddedEvent

### Test Event Replay

1. Delete projection data from MySQL
2. Restart query services
3. Verify data is rebuilt from event store

```bash
# Connect to MySQL
docker exec -it mysql-db mysql -uroot -proot

# Delete projection data
USE keynote_db;
DELETE FROM keynotes;

# Exit and restart query service
docker-compose restart keynote-query-service

# Verify data is restored
curl http://localhost:8888/keynotes/queries
```

## CQRS Tests

### Eventual Consistency Test

1. Create a keynote via command service
2. Immediately query the read model
3. Verify data appears within acceptable latency (< 1 second)

```bash
# Create keynote
KEYNOTE_ID=$(curl -s -X POST http://localhost:8888/keynotes/commands \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "User",
    "email": "test@example.com",
    "fonction": "Tester"
  }' | jq -r '.id')

# Wait briefly
sleep 1

# Query read model
curl http://localhost:8888/keynotes/queries/$KEYNOTE_ID
```

## Kafka Streams Tests

### Test Review Analytics

1. Create a conference
2. Add multiple reviews within 5 seconds
3. Check analytics endpoint for aggregated data

```bash
# Create conference
CONFERENCE_ID="test-conf-1"

# Add 3 reviews rapidly
for i in 1 2 3; do
  curl -X POST http://localhost:8888/conferences/commands/$CONFERENCE_ID/reviews \
    -H "Content-Type: application/json" \
    -d "{
      \"texte\": \"Review $i\",
      \"note\": $i
    }"
done

# Check analytics (wait 5 seconds for window)
sleep 6
curl http://localhost:8888/analytics/reviews/realtime
```

Expected: Analytics showing:
- totalReviews: 3
- sumOfNotes: 6
- averageNote: 2.0

## Service Discovery Tests

### Test Eureka Registration

1. Access Eureka Dashboard: http://localhost:8761
2. Verify all services are registered:
   - GATEWAY-SERVICE
   - KEYNOTE-COMMAND-SERVICE
   - KEYNOTE-QUERY-SERVICE
   - CONFERENCE-COMMAND-SERVICE
   - CONFERENCE-QUERY-SERVICE
   - ANALYTICS-SERVICE
   - AUTH-SERVICE

### Test Service Failure and Recovery

```bash
# Stop a service
docker-compose stop keynote-query-service

# Verify it's deregistered (wait 30 seconds)
# Check Eureka dashboard

# Restart service
docker-compose start keynote-query-service

# Verify it re-registers
```

## Load Testing

### Using Apache Bench

```bash
# Install Apache Bench (if not installed)
sudo apt-get install apache2-utils

# Load test keynote creation
ab -n 100 -c 10 -p keynote.json -T application/json \
  http://localhost:8888/keynotes/commands

# keynote.json content:
# {"nom":"Test","prenom":"User","email":"test@example.com","fonction":"Tester"}
```

### Using k6

```javascript
// load-test.js
import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 10,
  duration: '30s',
};

export default function () {
  let payload = JSON.stringify({
    nom: 'Test',
    prenom: 'User',
    email: `test${__VU}@example.com`,
    fonction: 'Tester'
  });

  let res = http.post('http://localhost:8888/keynotes/commands', payload, {
    headers: { 'Content-Type': 'application/json' },
  });

  check(res, {
    'status is 201': (r) => r.status === 201,
  });
}
```

Run:
```bash
k6 run load-test.js
```

## Performance Tests

### Response Time Benchmarks

| Endpoint | Expected Response Time |
|----------|------------------------|
| Create Keynote | < 200ms |
| Get All Keynotes | < 100ms |
| Create Conference | < 200ms |
| Add Review | < 150ms |
| Get Analytics | < 300ms |

### Throughput Tests

Expected throughput:
- Command operations: 100+ req/sec
- Query operations: 500+ req/sec

## Security Tests

### Authentication Tests

```bash
# Login
TOKEN=$(curl -X POST http://localhost:8888/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' | jq -r '.token')

# Use token
curl http://localhost:8888/keynotes/queries \
  -H "Authorization: Bearer $TOKEN"
```

### Authorization Tests

Test that:
1. Unauthenticated requests are rejected
2. Invalid tokens are rejected
3. Expired tokens are rejected

## Frontend Tests

### Manual Testing Checklist

- [ ] Can create a keynote
- [ ] Can view list of keynotes
- [ ] Can update a keynote
- [ ] Can delete a keynote
- [ ] Can create a conference
- [ ] Can view list of conferences
- [ ] Can add a review to conference
- [ ] Analytics dashboard updates in real-time
- [ ] Navigation between pages works
- [ ] Forms validate input
- [ ] Error messages display correctly

### Automated Frontend Tests

```bash
cd frontend
npm test
```

## Docker Deployment Tests

### Test Complete Deployment

```bash
# Deploy entire system
./deploy.sh

# Wait for all services to start
sleep 120

# Verify all containers are running
docker-compose ps

# Test basic functionality
curl http://localhost:8888/keynotes/queries
curl http://localhost:3000  # Should return frontend HTML
```

## Monitoring and Observability

### Health Checks

Check all service health endpoints:
```bash
for port in 8761 8888 8081 8082 8083 8084 8085 9999; do
  echo "Checking port $port..."
  curl http://localhost:$port/actuator/health
done
```

### Log Analysis

```bash
# View logs for all services
docker-compose logs -f

# View logs for specific service
docker-compose logs -f keynote-command-service

# Search for errors
docker-compose logs | grep ERROR
```

## Troubleshooting Common Issues

### Issue: Service not registering with Eureka
**Solution:** Check that Discovery Service is running and service configuration has correct Eureka URL

### Issue: Events not appearing in Axon Server
**Solution:** Verify Axon Server is running and services can connect (check port 8124)

### Issue: Kafka connection errors
**Solution:** Ensure Zookeeper is running before Kafka, wait for Kafka to fully start

### Issue: Database connection errors
**Solution:** Wait for MySQL to fully initialize (may take 60+ seconds on first start)

## Test Data Cleanup

```bash
# Stop all services
docker-compose down -v

# This removes all volumes including databases
# Restart for clean state
./deploy.sh
```

## Continuous Integration Tests

For CI/CD pipelines:

```yaml
# .github/workflows/test.yml
name: Test

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build with Maven
        run: mvn clean install -DskipTests
      - name: Run tests
        run: mvn test
```

## Performance Monitoring

Monitor key metrics:
- Response times
- Throughput (requests/sec)
- Error rates
- Event processing latency
- Kafka lag
- Database connection pool usage

Access metrics:
```bash
# Spring Boot Actuator metrics
curl http://localhost:8081/actuator/metrics
curl http://localhost:8082/actuator/metrics
```
