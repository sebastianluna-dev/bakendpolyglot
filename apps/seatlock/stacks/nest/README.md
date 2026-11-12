# SeatLock — NestJS + Prisma

NestJS 12 on Express 5, Prisma 7 (Rust-free client with the node-postgres adapter), `jose` for
tokens, `ioredis` for the cache, `class-validator` DTOs.

```
prisma/schema.prisma            maps the shared SQL schema; `prisma generate` → src/generated
src/auth/auth.guard.ts          global guard: JWKS + RS256 + iss + aud, then @Roles(); @Public() opts out
src/events/                     create (box-office), cached reads, seat map with fresh availability
src/holds/holds.service.ts      the atomic claim; reaper.service.ts the expiry job and its endpoint
src/orders/                     checkout (resumable) and the Idempotency-Key service
src/payments/                   provider client and the raw-body webhook
```

```bash
make test
DATABASE_URL=… OIDC_ISSUER=http://localhost:8081/realms/seatlock PAYMENTS_URL=http://localhost:4000 \
  PAYMENT_WEBHOOK_SECRET=whsec_dev make run
```

Things to notice (book chapters in brackets):

- **Deny by default**: `AuthGuard` is an `APP_GUARD`, so a new controller is protected until someone
  writes `@Public()` on it [Ch. 21].
- **The claim** is `$transaction` + `$executeRaw`: Prisma's typed API has no "update where status =
  'available' and tell me how many rows" across a list, so this one statement is SQL [Ch. 23].
- **Checkout uses `@Res()`** without passthrough: the status comes from the stored idempotency outcome,
  and Nest would otherwise force its default 201 [Ch. 24].
- **`express.raw()` on the webhook route only**, mounted before `express.json()` [Ch. 24].
- On machines that can't download Prisma's engines, `prisma generate` still works with
  `PRISMA_SCHEMA_ENGINE_BINARY=/bin/true PRISMA_ENGINES_CHECKSUM_IGNORE_MISSING=1`: generating the
  Rust-free client needs no engine.
