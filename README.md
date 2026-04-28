# 🚀 User Service (Spring Boot REST API Microservice)

A Spring Boot microservice that provides REST APIs for user management, authentication, and account handling.

---

# ✨ Features

- User Management (basic profile data)
- Authentication (Login / JWT-ready structure)
- Password Management (Change Password)
- RBAC-ready architecture
- MariaDB integration
- Environment-based configuration (dev / prod)
- Thread pool configuration support
- HikariCP connection pooling
- REST API-based microservice

---

# 📦 Prerequisites

## Development Requirements

- Java 17 (REQUIRED)
- Maven 3.8+
- Docker (optional)
- MariaDB (local or containerized)
- Git
- Visual Studio Code
- Spring Boot Extension Pack (VS Code)

## API Testing Tool

- Bruno (recommended)
  - REST API testing tool
  - Collection-based requests
  - Environment support

---

# 📁 Repository

git clone https://github.com/tlgc-it-dept/user-service.git  
cd user-service

---

# ⚙️ Environment Variables

Create a `.env` file in the root directory.

ACTIVE_PROFILE=dev  
→ dev = development  
→ prod = production

---

## Production Configuration

PROD_SPRING_DATASOURCE_URL=jdbc:mariadb://db:3306/user_service  
PROD_SPRING_DATASOURCE_USERNAME=root  
PROD_SPRING_DATASOURCE_PASSWORD=Password123  
PROD_SPRING_PORT=8080

---

## Development Configuration

DEV_SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306/user_service  
DEV_SPRING_DATASOURCE_USERNAME=root_dev  
DEV_SPRING_DATASOURCE_PASSWORD=password_for_dev  
DEV_SPRING_PORT=8080

---

## MariaDB Container

MARIADB_ROOT_PASSWORD=root  
MARIADB_PORT=3306

---

## Thread Pool Configuration

THREAD_CORE_POOL_SIZE=4  
THREAD_MAX_POOL_SIZE=50  
THREAD_QUEUE_CAPACITY=600  
THREAD_KEEP_ALIVE_MS=60000

---

## Database Pool Configuration

DB_MAX_POOL_SIZE=30  
DB_MIN_IDLE_CONNECTIONS=10  
DB_CONNECTION_LIFETIME_MS=3600000  
DB_CONNECTION_TIMEOUT_MS=30000

---

# 🧑‍💻 Development Setup

## 1. Clone Repository

git clone https://github.com/tlgc-it-dept/user-service.git  
cd user-service

---

## 2. Set Environment

ACTIVE_PROFILE=dev

Make sure MariaDB is running locally.

---

## 3. Run in VS Code

- Open project in VS Code
- Use Spring Boot Dashboard
- Click Run / Debug

---

## 4. Run via CLI

mvn clean compile  
mvn spring-boot:run

---

# 🧪 API Testing (Bruno)

- Open Bruno
- Import collection
- Set environment variables
- Send requests to:

http://localhost:8080

---

# 🚀 Deployment Guide

## Option 1: Full Deployment (App + Database)

ACTIVE_PROFILE=prod

mvn clean package -Dmaven.test.skip=true

sudo docker compose -f docker-compose.yaml up --build -d

---

## Option 2: Service Only Deployment

spring.profiles.active=prod

mvn clean package -Dmaven.test.skip=true

sudo docker compose -f docker-compose-service-only.yaml up --build -d

---

# 🧠 Important Notes

- Requires Java 17
- Do NOT mix dev and prod configs
- Ensure MariaDB is running before starting service
- Always match ACTIVE_PROFILE correctly
- Uses HikariCP for DB performance
- REST API microservice (no frontend)

---

# 🔥 Summary

- Spring Boot REST API microservice
- Java 17 required
- VS Code supported
- Bruno for API testing
- Docker Compose ready
- MariaDB supported
- Scalable architecture
