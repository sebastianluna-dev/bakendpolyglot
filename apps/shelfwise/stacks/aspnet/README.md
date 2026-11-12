# Shelfwise — ASP.NET Core (Minimal APIs + Npgsql)

.NET 10, Minimal APIs, `System.Text.Json`, and plain [Npgsql](https://www.npgsql.org/) with
`NpgsqlDataSource`. No EF Core here on purpose — the SQL matches the other stacks line for line; see
Chapter 21 for when EF Core earns its place.

```
src/Shelfwise/
  Program.cs          composition root: config, logging, DI, middleware, routes
  Config.cs           typed, validated environment config
  Domain/             records, DomainException, LoanService + ILoanStore, cursors   ← no ASP.NET, no SQL
  Data/               repositories (ADO.NET over Npgsql)
  Http/               Endpoints, Body (field-by-field validation), Problems (middleware)
tests/Shelfwise.Tests/  xUnit tests of the loan rules with a fake store
```

```bash
make test
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise make run
SHELFWISE_URL=http://localhost:3000 npm --prefix ../../conformance test
```

Things to notice (book chapters in brackets):

- **Primary constructors** on services and repositories make constructor injection one line [Ch. 6].
- **`TimeProvider`** is the framework's clock abstraction; tests pass a fixed one [Ch. 37].
- **Ids bind as `string`** and are parsed by hand, so a bad id is Shelfwise's `invalid-id` problem rather
  than the framework's own 400 [Ch. 12].
- **Exceptions for domain errors**: idiomatic in C#. One middleware turns `DomainException` into Problem
  Details; everything else is logged and becomes a 500 [Ch. 14].
- `DATABASE_URL` is a URI; Npgsql wants `Host=…;Username=…`, so `Sql.ConnectionString` converts it.
