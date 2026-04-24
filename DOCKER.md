# Docker Setup Guide - GestionDeProjets

This guide explains how to run the complete GestionDeProjets stack using Docker.

## Prerequisites

- Docker Desktop installed (includes Docker Engine and Docker Compose)
- Git
- At least 4GB of free disk space

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/Ala-Fredj24/GestionDeProjets.git
cd GestionDeProjets
```

### 2. Configure Environment (Optional)

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` to customize:
- Database credentials
- Ports (default: MySQL 3306, Backend 8085, Frontend 4200, phpMyAdmin 8084)
- Spring profile (default: docker)

### 3. Build and Start the Stack

```bash
# Build images and start all services
docker-compose up --build

# Or start in detached mode
docker-compose up -d --build
```

### 4. Access Applications

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8085/api
- **Swagger Documentation**: http://localhost:8085/api/swagger-ui.html
- **phpMyAdmin**: http://localhost:8084
  - Username: `appuser`
  - Password: `apppass`

## Available Commands

### Start Services

```bash
# Start all services
docker-compose up

# Start in background
docker-compose up -d

# Build before starting
docker-compose up --build

# Start specific service
docker-compose up mysql
docker-compose up backend
docker-compose up frontend
```

### Stop Services

```bash
# Stop all running services
docker-compose stop

# Stop and remove containers
docker-compose down

# Remove containers and volumes
docker-compose down -v
```

### View Logs

```bash
# View all logs
docker-compose logs

# Follow logs in real-time
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### Inspect Services

```bash
# List running containers
docker-compose ps

# Execute command in container
docker-compose exec backend sh
docker-compose exec frontend sh
docker-compose exec mysql bash

# View service health
docker-compose ps
```

## Troubleshooting

### Port Already in Use

If ports are already in use, modify `.env`:

```env
MYSQL_PORT=3307
BACKEND_PORT=8086
FRONTEND_PORT=4201
```

Then restart:

```bash
docker-compose down
docker-compose up -d
```

### Database Connection Issues

Check MySQL is running and healthy:

```bash
docker-compose ps mysql
docker-compose logs mysql
```

Verify environment variables in docker-compose.yml match your setup.

### Backend Build Fails

If Maven build fails in Docker:

```bash
# Build locally first
cd backend
mvn clean package -DskipTests
cd ..

# Then build Docker image
docker-compose build --no-cache backend
```

### Frontend Assets Not Loading

Clear browser cache and nginx cache:

```bash
# Restart frontend
docker-compose restart frontend

# Or rebuild
docker-compose up --build frontend
```

## Development Workflow

### With Live Reload

For development, you can still use local npm/maven while keeping MySQL in Docker:

```bash
# Start only MySQL and phpMyAdmin
docker-compose up -d mysql phpmyadmin

# Run backend locally
cd backend
mvn spring-boot:run

# In another terminal, run frontend locally
cd frontend
npm start
```

### Building for Production

Update docker-compose.yml to use production images:

```bash
# Build optimized images
docker-compose build

# Run with production settings
SPRING_PROFILE=production docker-compose up -d
```

## Security Notes

⚠️ **DO NOT use the example credentials in production!**

For production:
1. Use strong passwords in `.env`
2. Use Docker secrets for sensitive data
3. Enable SSL/TLS
4. Restrict database access
5. Use container security scanning
6. Implement logging and monitoring

Example production setup:

```yaml
# Use docker-compose.prod.yml with:
- Hardened MySQL image
- Non-root user execution
- Resource limits
- Health checks
- Logging configuration
- Network policies
```

## Network Architecture

```
┌─────────────────────────────────────────┐
│      gestion-network (bridge)           │
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────┐  ┌──────────┐            │
│  │ Frontend │──│ Backend  │            │
│  │ (4200)   │  │ (8085)   │            │
│  └──────────┘  └──────────┘            │
│       │              │                  │
│       └──────────────┤                  │
│                 ┌────▼────┐             │
│                 │  MySQL   │             │
│                 │ (3306)   │             │
│                 └──────────┘             │
│                      │                  │
│                 ┌────▼────┐             │
│                 │ phpMyAdmin│            │
│                 │ (8084)    │            │
│                 └──────────┘             │
│                                         │
└─────────────────────────────────────────┘
```

## Performance Tips

1. Use .dockerignore to reduce build context
2. Use multi-stage builds (already implemented)
3. Adjust Docker Desktop memory/CPU allocations
4. Use volume mounts for development
5. Enable BuildKit for faster builds: `export DOCKER_BUILDKIT=1`

## Additional Resources

- Docker Compose Documentation: https://docs.docker.com/compose/
- Spring Boot Docker: https://spring.io/guides/topicals/spring-boot-docker/
- Angular Docker: https://angular.io/guide/docker
- MySQL Docker: https://hub.docker.com/_/mysql

## Support

For issues or questions:
1. Check logs: `docker-compose logs -f [service]`
2. Verify environment variables: `docker-compose config`
3. Restart services: `docker-compose restart`
4. Review application health: `docker-compose ps`
