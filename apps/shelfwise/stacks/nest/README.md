# Shelfwise · NestJS

The same service as the Express stack, organised the Nest way: modules, injectable providers, class-validator DTOs, a global `ValidationPipe` and one exception filter (Chapter 3).

## Run

```bash
npm install && npm run build
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise npm start
# or, from apps/shelfwise:  docker compose --profile nest up --build
```

Same variables as every stack: `DATABASE_URL` (required), `PORT`, `LOAN_DAYS`, `MAX_LOANS_PER_MEMBER`.

## Reading guide: follow `POST /loans`

1. **`src/main.ts`** creates the app, installs the JSON body parser (so malformed JSON is a 400 problem), the global `ValidationPipe` and the `ProblemFilter`, and enables shutdown hooks.
2. **`src/app.module.ts`** imports a global `ConfigModule` (zod-validated settings under the `CONFIG` token), `DatabaseModule` (the `pg` pool under `POOL`) and the three feature modules.
3. **`src/loans/loans.module.ts`**: `LoansController.lend` receives a validated `CreateLoanRequest`. The module builds `LoanService` with a **factory provider**, because the domain service is plain TypeScript with no Nest decorators.
4. **`src/domain/loans.ts`** is byte-for-byte the Express stack’s rules: the domain does not know which framework hosts it (Chapter 28).
5. **`src/loans/loans.repository.ts`** implements the `LoanStore` port with SQL; a unique violation becomes `copyOnLoan()`.
6. **`src/common/problem.filter.ts`** renders `DomainError` and Nest’s `HttpException` as Problem Details.

## Compared with the Express stack

- Wiring moves from `main.ts` into module metadata; the container builds the graph and every provider is a singleton.
- Validation moves from explicit `parse()` calls into DTO classes and a global pipe. `whitelist` plus `forbidNonWhitelisted` reject unknown fields.
- `useDefineForClassFields: false` in `tsconfig.json` matters: without it, DTO class fields would exist as `undefined` own properties and a partial `PATCH` could not tell "absent" from "set".
- NestJS 12 ships as ES modules, so this stack is `"type": "module"` with `.js` import suffixes.
