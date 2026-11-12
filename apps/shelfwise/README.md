# Shelfwise

The library catalog and loans API from **Part II** of *Backend Polyglot* (Chapter 18): one contract, one schema, one conformance suite, and one implementation per stack.

| Folder | What it is |
| --- | --- |
| `contract/openapi.yaml` | The API contract every stack implements |
| `db/migrations/` | The shared PostgreSQL schema (Chapter 14) |
| `conformance/` | HTTP-only tests (Vitest) that every stack must pass |
| `stacks/<stack>/` | One implementation per stack, each with its own README, unit tests, Dockerfile and Makefile |
| `compose.yaml` | Postgres with the schema, plus each stack behind a profile |

## Stacks

| Stack | Status |
| --- | --- |
| `express` (Express 5, pg, zod) | ✅ passes the conformance suite |
| `nest` (NestJS 12, pg, class-validator) | ✅ passes the conformance suite |
| `fastapi` (FastAPI, asyncpg, Pydantic) | ✅ passes the conformance suite |
| `django` (Django 6, DRF, psycopg 3) | ✅ passes the conformance suite |
| `go` (`net/http`, pgx) | ✅ HTTP layer passes the suite on the in-memory store; Postgres run in CI |
| `aspnet` (.NET 10 Minimal APIs, Npgsql) | Written; verified in CI |
| `spring-java` (Spring Boot 3.5, JdbcClient) | Written; verified in CI |
| `spring-kotlin` (Spring Boot 3.5, Kotlin 2.1) | Written; verified in CI |

## Run a stack and check it

```bash
docker compose --profile express up --build -d --wait
npm --prefix conformance ci
SHELFWISE_URL=http://localhost:3000 npm --prefix conformance test
```

Every stack exposes the same Makefile targets (`install`, `lint`, `build`, `test`, `run`), which is what CI calls.

## Adding a stack

1. Create `stacks/<name>/` with a Makefile exposing the five targets and a Dockerfile serving port 3000.
2. Read the configuration from `DATABASE_URL`, `PORT`, `LOAN_DAYS` and `MAX_LOANS_PER_MEMBER`.
3. Add a profile to `compose.yaml` and the name to the CI matrix in `.github/workflows/shelfwise.yml`.
4. Make the conformance suite pass. Error bodies must be Problem Details with the problem codes listed in the contract.
