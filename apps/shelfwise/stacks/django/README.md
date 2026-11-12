# Shelfwise · Django

Django 6 with Django REST Framework and psycopg 3, served by Gunicorn (WSGI) with threads.

## Run

```bash
uv sync
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise make run
# or, from apps/shelfwise:  docker compose --profile django up --build
```

Same variables as every stack: `DATABASE_URL` (required), `LOAN_DAYS` (21), `MAX_LOANS_PER_MEMBER` (5). The port is set on the Gunicorn command line.

## Reading guide: follow `POST /loans`

1. **`shelfwise/settings.py`** reads everything environment-specific from variables and fails at import if they are missing or out of range. DRF is configured with no authentication, JSON only, and a custom exception handler.
2. **`shelfwise/urls.py`** maps paths to class-based `APIView`s. Ids are captured as strings so that `/books/abc` can be a 400, as in every other stack.
3. **`catalog/views.py`**: `LoanCreate.post` validates with `CreateLoanSerializer` and calls `loan_service.lend`.
4. **`catalog/serializers.py`**: DRF serializers as input DTOs. DRF ignores unknown fields by default; `StrictSerializer` rejects them.
5. **`catalog/loans.py`** holds the rules, in plain Python, identical in shape to the FastAPI stack’s but synchronous.
6. **`catalog/repos.py`** uses the ORM: `select_related("copy__book")` loads a loan with its copy and book in one query (Chapter 15). psycopg 3 exposes the violated constraint, so a unique violation on `one_open_loan_per_copy` becomes `copy_on_loan()`.
7. **`catalog/problems.py`** turns domain errors and DRF exceptions into Problem Details.

## Notes

- **The models are `managed = False`.** The schema belongs to `db/migrations`, shared by eight stacks, so Django maps the existing tables instead of generating migrations. In a Django-only project you would let `makemigrations` own the schema (Chapter 15).
- **No admin here.** Django’s admin needs its own tables (auth, sessions); adding it to this API is a good exercise, and the usual reason teams pick Django (Chapter 8).
- **Concurrency** comes from Gunicorn workers and threads, not `async`: the conformance suite’s eight simultaneous loans hit different threads and the partial unique index decides the winner.
