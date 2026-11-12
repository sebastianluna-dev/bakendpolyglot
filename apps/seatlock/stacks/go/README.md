# SeatLock — Go (`net/http` + pgx)

The standard-library router, pgx, `golang-jwt` + `keyfunc` for tokens, and `go-redis` with
`singleflight` for the cache. No framework.

```
cmd/seatlock/        wiring, the reaper ticker, -healthcheck for the distroless image
internal/domain/     types, errors and the ports (Store, Verifier, Cache)    ← no dependencies
internal/httpapi/    routes, auth middleware, validation, idempotency, webhooks   ← stdlib only
internal/postgres/   every SQL statement
internal/auth/       golang-jwt + keyfunc
internal/cache/      go-redis + singleflight
```

```bash
make test        # handler tests run against fakes: no database, no identity provider
```

Things to notice (book chapters in brackets):

- **`require(role, handler)`** wraps each protected route; the public ones are the four registered
  with plain `HandleFunc` [Ch. 21].
- **`pgx.BeginFunc`** rolls back when the function returns an error, so "fewer rows than requested"
  is just `return errSeatsTaken` [Ch. 23].
- **The fake store embeds `domain.Store`**: a test implements only the methods it calls [Ch. 37].
- **The reaper** is one SQL statement with data-modifying CTEs and `SKIP LOCKED` [Ch. 24].
