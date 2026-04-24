# Docker Setup — Personal Microservices Project

## Services & Ports

| Service         | Container Name  | Port  |
|-----------------|-----------------|-------|
| MySQL           | mysql           | 3306  |
| Eureka Server   | eruke-server    | 8761  |
| Config Server   | config-server   | 8888  |
| API Gateway     | api-gateway     | 8080  |
| Auth Service    | auth-service    | 8083  |
| User Service    | user-service    | 8081  |
| Order Service   | order-service   | 8082  |

## Startup Order (handled automatically by docker-compose)

```
MySQL ──► eruke-server ──► config-server ──► user-service ──► order-service
                      └──────────────────► auth-service
                      └──────────────────► api-gateway
```

## Commands

### Build & start everything
```bash
docker-compose up --build
```

### Start in detached mode
```bash
docker-compose up --build -d
```

### View logs for a specific service
```bash
docker-compose logs -f order-service
```

### Stop all services
```bash
docker-compose down
```

### Stop and remove volumes (wipes MySQL data)
```bash
docker-compose down -v
```

### Rebuild a single service
```bash
docker-compose up --build -d order-service
```

## Useful URLs (after startup)

- Eureka Dashboard → http://localhost:8761
- Config Server    → http://localhost:8888/actuator/health
- API Gateway      → http://localhost:8080
- Auth Login       → http://localhost:8080/auth/login
- Users API        → http://localhost:8080/users
- Orders API       → http://localhost:8080/orders

## Notes

- Secrets are stored in `.env` — **do not commit this to Git in production**.
- All services use multi-stage Docker builds (Maven build inside Docker, no local Maven needed).
- `localhost` URLs in `application.yaml` are for local dev only; Docker overrides them via environment variables set in `docker-compose.yml`.
