# Shelfwise · FastAPI

FastAPI, asyncpg and Pydantic, managed with uv. Dependencies are wired by hand in `create_app`; FastAPI's `Depends` is not needed for a service this small.

## Run

```bash
uv sync
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise make run
# or, from apps/shelfwise:  docker compose --profile fastapi up --build
```

Same variables as every stack: `DATABASE_URL` (required), `PORT` (3000 in the container), `LOAN_DAYS` (21), `MAX_LOANS_PER_MEMBER` (5).

## Reading guide: follow `POST /loans`

1. **`src/shelfwise/main.py`**: `create_app` validates `Settings`, opens the asyncpg pool in the lifespan, wires repositories and the `LoanService`, installs error handlers and the request-id middleware.
2. **`src/shelfwise/http/routes.py`**: `create_loan` receives a `CreateLoanRequest`; FastAPI has already parsed and validated it from the type hints.
3. **`src/shelfwise/http/models.py`**: the Pydantic DTOs. `extra="forbid"` rejects unknown fields; validators add rules such as "year cannot be in the future".
4. **`src/shelfwise/domain/loans.py`**: the rules, in plain Python. `LoanStore` is a `Protocol`, so the Postgres adapter satisfies it without inheriting from it.
5. **`src/shelfwise/db/repos.py`**: SQL with asyncpg. `UniqueViolationError` on `one_open_loan_per_copy` becomes `copy_on_loan()`.
6. **`src/shelfwise/http/problems.py`**: domain errors, validation errors and unknown routes all become `application/problem+json`.

## From TypeScript

- Every endpoint is `async def` and every database call is awaited: blocking calls here would stall the event loop exactly as in Node (Chapter 8).
- Validation errors default to FastAPI's own 422 shape; `problems.py` rewrites them into the Problem Details body every Shelfwise stack returns.
- asyncpg returns `bigint` as Python `int`, which has arbitrary precision, so there is no string conversion as in `pg`.
