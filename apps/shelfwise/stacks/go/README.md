# Shelfwise — Go (`net/http` + pgx)

The standard-library router (Go 1.22+ method and path patterns), `encoding/json`, `log/slog`, and
[pgx](https://github.com/jackc/pgx) for Postgres. No framework: this is what most Go shops ship.

```
cmd/shelfwise/       production binary (Postgres)
cmd/shelfwise-mem/   same HTTP layer on an in-memory store — no database needed
internal/domain/     types, errors, loan rules, ports (interfaces)    ← no dependencies
internal/httpapi/    handlers, validation, Problem Details             ← stdlib only
internal/postgres/   pgx implementation of the ports
internal/memstore/   map-and-mutex implementation of the same ports
internal/app/        config, logging, graceful shutdown
```

```bash
make test                                   # domain + handler tests (httptest)
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise make run
make dev                                    # in-memory, port 3000
SHELFWISE_URL=http://localhost:3000 npm --prefix ../../conformance test
```

Things to notice (book chapters in brackets):

- **Interfaces are declared by the consumer** — `domain.LoanStore` lives next to the service that
  needs it, and two packages satisfy it without saying so [Ch. 7].
- **Errors are values**: storage maps `pgconn.PgError` constraint names to `*domain.Error`; one
  `writeError` turns those into Problem Details and everything else into a logged 500 [Ch. 14].
- **`*int` and `YearSet`** distinguish "absent" from "null" in PATCH, which `encoding/json` alone
  cannot do — hence the small `readObject` helper over `map[string]json.RawMessage` [Ch. 12].
- **Server timeouts and `Shutdown`** are set explicitly; the zero-value `http.Server` has none [Ch. 41].
- The image is distroless, so the compose health check runs `shelfwise -healthcheck`.
