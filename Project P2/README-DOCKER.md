# RevHub Docker Setup

## Prerequisites
- Docker Desktop installed
- Docker Compose installed
- 8GB RAM minimum

## Build All Services
```bash
# Build all backend services
cd backend
mvn clean install -DskipTests

# Or build individually
cd user-service && mvn clean install -DskipTests
cd post-service && mvn clean install -DskipTests
# ... repeat for all services
```

## Run with Docker Compose

### Start All Services
```bash
docker-compose up -d
```

### Stop All Services
```bash
docker-compose down
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service
```

### Rebuild After Code Changes
```bash
# Rebuild specific service
docker-compose up -d --build user-service

# Rebuild all
docker-compose up -d --build
```

## Access Application
- **Frontend**: http://localhost
- **API Gateway**: http://localhost:8080
- **Consul UI**: http://localhost:8500

## Service Ports
- Frontend: 80
- API Gateway: 8080
- User Service: 8081
- Post Service: 8082
- Follow Service: 8083
- Feed Service: 8084
- Notification Service: 8085
- Chat Service: 8086
- Search Service: 8087
- Config Server: 8888
- MySQL: 3306
- MongoDB: 27017
- Consul: 8500

## Troubleshooting

### Services not starting
```bash
# Check logs
docker-compose logs

# Restart specific service
docker-compose restart user-service
```

### Database connection issues
```bash
# Ensure MySQL is ready
docker-compose logs mysql

# Recreate database
docker-compose down -v
docker-compose up -d
```

### Clear all data
```bash
docker-compose down -v
docker system prune -a
```

## Production Deployment
1. Update environment variables in docker-compose.yml
2. Use external database (not containerized)
3. Add health checks
4. Configure resource limits
5. Use Docker Swarm or Kubernetes for orchestration
