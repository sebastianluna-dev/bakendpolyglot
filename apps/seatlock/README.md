# SeatLock

The event ticketing API from **Part III** of *Backend Polyglot* (Chapter 27): authenticated users from
Keycloak, a scarce resource fought over by concurrent buyers, checkout with idempotency keys, payments
confirmed by signed webhooks, an expiring-hold reaper, and a Redis cache in front of the reads. One
contract, one schema, one conformance suite, five stacks.

| Folder | What it is |
| --- | --- |
| `contract/openapi.yaml` | The API contract, including every problem code |
| `db/migrations/` | The shared PostgreSQL schema (Chapters 23–24) |
| `db/checks/invariants.sql` | The Phase 3 gate: must return zero rows after any load test |
| `keycloak/seatlock-realm.json` | The realm: clients, roles (`customer`, `box-office`) and test users (Chapter 20) |
| `tools/payments-fake/` | A payment provider that sends signed webhooks, sometimes twice, on purpose |
| `tools/dev-idp/` | A Keycloak-shaped token issuer for machines without Docker |
| `conformance/` | 43 HTTP-only tests (Vitest) every stack must pass |
| `load/` | The k6 scenario for the gate, and the same in plain Node |
| `stacks/<stack>/` | One implementation per stack, each with README, unit tests, Dockerfile and Makefile |

## Stacks

| Stack | Libraries | Status |
| --- | --- | --- |
| `nest` | NestJS 12, Prisma 7, jose, ioredis | ✅ conformance and invariants under load |
| `django` | Django 6 ORM, DRF, PyJWT, Redis cache | ✅ conformance and invariants under load |
| `go` | `net/http`, pgx, golang-jwt + keyfunc, go-redis + singleflight | ✅ conformance in CI |
| `aspnet` | .NET 10 controllers, EF Core, JwtBearer, HybridCache | ✅ conformance in CI |
| `spring-java` | Spring Boot 3.5, Spring Security, Spring Data JPA, Spring Cache on Redis | ✅ conformance in CI |

## Run a stack and check it

```bash
HOLD_TTL_SECONDS=4 docker compose --profile nest up --build -d --wait   # Postgres, Redis, Keycloak, payments, Nest
npm --prefix conformance ci
npm --prefix conformance test
```

Without Docker: start Postgres and Redis yourself, apply `db/migrations`, run `node tools/dev-idp/server.mjs`
and `node tools/payments-fake/server.mjs`, then the stack with the environment below.

## The gate (Chapter 27)

```bash
AUTO_WEBHOOKS=1 SEATLOCK_STACK=nest docker compose --profile nest up --build -d --wait
k6 run load/checkout.k6.js                     # or: node load/stress.mjs 1000
psql postgres://app:app@localhost:5432/seatlock -f db/checks/invariants.sql   # zero rows
```

## Configuration (every stack)

| Variable | Default | Meaning |
| --- | --- | --- |
| `DATABASE_URL` | — | `postgres://user:pass@host:5432/db` |
| `OIDC_ISSUER` | — | The `iss` tokens must carry, e.g. `http://localhost:8081/realms/seatlock` |
| `OIDC_JWKS_URL` | issuer + `/protocol/openid-connect/certs` | Where keys are fetched (may be an internal URL) |
| `OIDC_AUDIENCE` | `seatlock-api` | Required `aud` |
| `REDIS_URL` | none (no cache) | Shared cache for event pages and seat layouts |
| `PAYMENTS_URL`, `PAYMENT_WEBHOOK_SECRET` | — | Provider base URL and HMAC secret |
| `HOLD_TTL_SECONDS` | 600 | Hold lifetime |
| `REAPER_INTERVAL_SECONDS` | 60 | In-process reaper; 0 disables it |
| `MAX_SEATS_PER_HOLD` | 6 | |
