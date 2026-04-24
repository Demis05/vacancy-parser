# INSTALL

## Requirements

- Java 17
- PostgreSQL
- Docker

## Database setup

### Option 1. Docker

Run PostgreSQL from the project root:

```bash
docker compose up -d
```

This starts:

- database: `vacancy_parser`
- username: `postgres`
- password: `postgres`

### Option 2. Manual local PostgreSQL

Create database manually:

```sql
CREATE DATABASE vacancy_parser;
```

Default creds in `application.yml`:

- username: `postgres`
- password: `postgres`

## Configure application

Database settings are stored in:

- [application.yml](/Users/demistheodoridis/Desktop/vacancy-parser/src/main/resources/application.yml)

You can keep the default values or override them with environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Run project

Build:

```bash
mvn clean package
```

Start application:

```bash
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```

## Import data

Trigger Techstars import:

```bash
curl -X POST http://localhost:8080/api/import
```

Expected response example:

```json
{
  "retrieved": 20,
  "created": 20,
  "updated": 0,
  "deactivated": 0
}
```

## API endpoints

Get all vacancies:

```bash
curl "http://localhost:8080/api/vacancies"
```

Get vacancies with pagination:

```bash
curl "http://localhost:8080/api/vacancies?page=0&size=10"
```

Get vacancies with filters:

```bash
curl "http://localhost:8080/api/vacancies?company=LogicGate&location=United%20States&tag=Software"
```

Get vacancy by id:

```bash
curl "http://localhost:8080/api/vacancies/1"
```

## Swagger path

Open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Open OpenAPI JSON:

```text
http://localhost:8080/api-docs
```

## What is stored in vacancy 

- job title
- company name
- location
- description
- Techstars vacancy link
- source apply link
- tags
- skills
- salary metadata
- work mode
- seniority
- timestamps and sync status
