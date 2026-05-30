# 🧑‍💻 User Service (Spring Boot REST API Microservice)

A Spring Boot microservice that provides REST APIs for user management, authentication, and account handling.

---

# ✨ Features

- User Management (basic profile data)
- Authentication (Login / JWT-ready structure)
- Password Management (Change Password)
- RBAC-ready architecture
- MySQL + JPA persistence
- Environment-based configuration (dev / docker / prod)
- Thread pool configuration support
- HikariCP connection pooling
- REST API-based microservice

---

# 📦 Prerequisites

## Development Requirements

- Java 17 (REQUIRED)
- Maven 3.8+
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

This service can load environment values from a root `.env` file when using the **dev** profile.

Create a `.env` file in the service root directory:

```properties
ACTIVE_PROFILE=dev
DB_USERNAME=root_dev
DB_PASSWORD=password_for_dev
```

> `application-dev.properties` uses `spring.config.import=file:.env[.properties]`.

---

## Database (what the profiles expect)

### 1) `dev` (local database)

- `spring.datasource.url` is fixed to:
  - `jdbc:mysql://localhost:3306/user_service`
- Required:
  - `DB_USERNAME`
  - `DB_PASSWORD`

### 2) `docker` (container network)

Used by `application-docker.properties`.

Required:

- `DB_HOST` (e.g., a MySQL container name)
- `DB_USERNAME`
- `DB_PASSWORD`

Also set:

- `SPRING_PROFILES_ACTIVE=docker`

### 3) `prod` (external injection only)

Used by `application-prod.properties`.

Required:

- `DB_URL` (full JDBC URL)
- `DB_USER`
- `DB_PASSWORD`

Optional tuning (with defaults matching the properties file):

- `DB_MAX_POOL_SIZE` (default: 30)
- `DB_MIN_IDLE` (default: 10)
- `DB_MAX_LIFETIME` (default: 1800000)
- `DB_TIMEOUT` (default: 30000)
- `THREAD_CORE` (default: 8)
- `THREAD_MAX` (default: 16)
- `THREAD_QUEUE` (default: 200)
- `THREAD_KEEPALIVE` (default: 60000)

---

# 🧑‍💻 Development Setup

## Option 1: Run Locally (VS Code / Maven dev mode)

### A: Using VS Code (Recommended)

1. Open the project folder in VS Code.
2. Use the Spring Boot Dashboard.
3. Click **Run / Debug** on the `user-service` application.

### B: Using Terminal (Maven)

1. Start a MySQL database locally.
2. Ensure your `.env` file contains the required `DB_USERNAME` and `DB_PASSWORD`.
3. Run:

```bash
mvn clean compile
mvn spring-boot:run
```

---

## Option 2: Run Using Docker Container (profile: docker)

This mode uses `application-docker.properties` and expects the app to connect to a MySQL instance via `DB_HOST`, `DB_USERNAME`, and `DB_PASSWORD`.

### Step 1: Build the JAR

```bash
mvn clean package -DskipTests
```

### Step 2: Build the Docker image

```bash
docker build --no-cache -t user-service .
```

### Step 3: Start the container

Example (MySQL on the same Docker network):

```bash
docker run -d --name user-service \
  -p 8081:8081 \
  -e DB_HOST=mysql \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=secret \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e JWT_SECRET=REPLACE_ME_WITH_BASE64 \
  -e JWT_EXPIRATION=3600000 \
  user-service
```

JWT secret notes:

- `JwtUtil` expects properties `JWT.SECRET` and `JWT.EXPIRATION`.
- Because this runs in Docker without changing source code, provide them via env vars:
  - `JWT_SECRET` -> `JWT.SECRET`
  - `JWT_EXPIRATION` -> `JWT.EXPIRATION`
- `JWT.SECRET` must be **Base64-encoded**, since `JwtUtil` does `Decoders.BASE64.decode(secret)`.

If MySQL is reachable from the host, `DB_HOST` should be set accordingly (e.g., `host.docker.internal`).

---

## Option 3: Run in Production Mode (external database)

This mode uses `application-prod.properties` (JPA validation, production-safe logging).

### Step 1: Build the JAR

```bash
mvn clean package -DskipTests
```

### Step 2: Run with prod profile

```bash
java -jar target/user-service.jar --spring.profiles.active=prod
```

### Docker example (prod)

```bash
docker run -d --name user-service-prod \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://external-host:3306/user_service \
  -e DB_USER=your_user \
  -e DB_PASSWORD=your_password \
  user-service
```

---

# 🧪 API Testing (Bruno)

- Open Bruno
- Import the `user-service-api-tests` collection
- Set environment variables in Bruno
- Send requests to:

http://localhost:8081

---

# 🧠 Important Notes

- Requires Java 17
- Profile-specific configuration is important (`dev` / `docker` / `prod`)
- `dev` loads `.env` from the project root
- `docker` expects `DB_HOST` to be resolvable from the container network
- `prod` uses external JDBC injection (`DB_URL`, `DB_USER`, `DB_PASSWORD`)
- Uses HikariCP for DB performance
- REST API microservice (no frontend)

---

# 🔥 Summary

- Spring Boot REST API microservice
- Java 17 required
- VS Code supported
- Bruno for API testing
- Docker run instructions included
- MySQL-backed persistence
