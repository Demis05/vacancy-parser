# vacancy-parser

Spring Boot application that imports vacancies from the Techstars job board, stores them in PostgreSQL, and exposes a REST API for reading saved vacancies.

## Description

The application:

- parses vacancies from `https://jobs.techstars.com/jobs`
- stores data in PostgreSQL using JPA/Hibernate
- updates existing vacancies by external id due import process
- creates new vacancies
- marks vacancies as `INACTIVE` when they are no longer present on the source site
- provides REST endpoints for manual import and vacancy retrieval

## Main features

- manual import trigger via API
- get vacancy by id
- get all vacancies with pagination
- optional filtering by `company`, `location`, and `tag`

## Tech stack

- Java 17
- Spring Boot 3
- Maven
- PostgreSQL
- jsoup
- MapStruct

## API endpoints

- `POST /api/import`
- `GET /api/vacancies`
- `GET /api/vacancies/{id}`

## Swagger

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Data dump

- Manual data dump file: `vacancies_dump.csv`
- The file contains exported active vacancies after a successful import run

## Notes

- The parser uses embedded `__NEXT_DATA__` JSON from Techstars pages instead of a browser UI.
- The API reads vacancies from the local database and does not depend on live parsing during `GET` requests.
- Missing vacancies are marked as `INACTIVE` instead of being physically deleted.

## Local database with Docker

Start PostgreSQL:

```bash
docker compose up -d
```

Stop PostgreSQL:

```bash
docker compose down
```
