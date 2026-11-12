# Shelfwise · Express

Express 5, `pg` and zod. No framework container: dependencies are wired by hand in `src/main.ts`.

## Run

```bash
npm install
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise npm run dev
# or, from apps/shelfwise:  docker compose --profile express up --build
```

| Variable | Default | Meaning |
| --- | --- | --- |
| `DATABASE_URL` | required | PostgreSQL connection string |
| `PORT` | `3000` | HTTP port |
| `LOAN_DAYS` | `21` | Days until a loan is due |
| `MAX_LOANS_PER_MEMBER` | `5` | Open loans allowed per member |

`make test` runs the unit tests; the conformance suite in `../../conformance` checks the HTTP contract.

## Reading guide: follow `POST /loans`

1. **`src/main.ts`** is the composition root: config, pool, repositories, the `LoanService`, the app.
2. **`src/app.ts`** builds the pipeline: request id and access log, `express.json()`, routes, then the problem handler.
3. **`src/http/routes.ts`** is the handler layer. `POST /loans` parses the body with `CreateLoanRequest` (a zod schema in `src/http/validation.ts`) and calls `loanService.lend`.
4. **`src/domain/loans.ts`** holds the rules: copy and member exist, copy not on loan, member under the limit, due date from the clock. It depends on the `LoanStore` interface, not on SQL.
5. **`src/db/loans.ts`** implements `LoanStore`. A second open loan for the same copy violates the partial unique index `one_open_loan_per_copy`; the repository turns that `23505` into `copyOnLoan()`.
6. **`src/http/problems.ts`** turns every `DomainError` into `application/problem+json`. Express 5 forwards rejected promises from async handlers here without wrappers.

## From TypeScript

You know this stack, so the notes are about choices rather than surprises:

- `pg` returns `bigint` columns as strings to avoid precision loss; `src/db/pool.ts` parses them to numbers because Shelfwise ids stay far below 2^53.
- Express generates weak ETags by default. Shelfwise turns that off and sets version-based ETags itself, so `If-Match` can be checked against the database.
- The same structure without the hand wiring, using modules and DI, is the NestJS stack.
