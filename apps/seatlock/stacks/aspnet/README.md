# SeatLock — ASP.NET Core (controllers + EF Core)

.NET 10 with **controllers** (Chapter 4 promised a controller-based stack; Shelfwise used Minimal
APIs), EF Core on Npgsql, JwtBearer, and HybridCache with Redis as its second level.

```
Program.cs              composition root: auth, EF Core, HybridCache, HttpClient, reaper worker
Config.cs               env → validated AppConfig
Http/Auth.cs            JwtBearer: keys from the JWKS URL, roles flattened from realm_access
Http/Body.cs            field-by-field validation with nested paths (sections[0].rows)
Controllers/            Events, Holds (incl. checkout and the reaper endpoint), Webhooks
Services/               EventService (cached reads), HoldService (the claim, the reaper),
                        CheckoutService, IdempotencyService, PaymentEvents, ReaperWorker
Data/SeatLockDb.cs      entities mapped to the shared schema (no EF migrations)
```

Things to notice (book chapters in brackets):

- **The claim** is the book's EF Core version: a transaction, `SaveChanges` for the hold, then one
  `ExecuteUpdateAsync` whose row count decides; leaving the `using` without commit rolls back [Ch. 23].
- **Row locks** use `FromSql(... FOR UPDATE)` materialised with `ToListAsync()`, so EF does not
  wrap the locking query in a subquery.
- **JwtBearer without discovery**: a tiny `IConfigurationRetriever` reads the JWKS URL directly, so
  the issuer can be the public URL while keys come over the internal network [Ch. 19].
- **401 and 403 bodies** are written in `OnChallenge` / `OnForbidden`, so auth failures are
  Problem Details too [Ch. 12].
