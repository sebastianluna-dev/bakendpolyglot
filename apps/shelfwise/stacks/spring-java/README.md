# Shelfwise — Spring Boot (Java 21)

Spring Boot 3.5, Spring MVC on virtual threads, `JdbcClient` over HikariCP, and pgJDBC. No JPA here
on purpose: the SQL matches the other stacks line for line (Chapter 21 covers when JPA earns its place).

```
src/main/java/dev/shelfwise/
  ShelfwiseApplication.java   entry point, beans (LoanService, Clock), self health check
  AppConfig.java              env vars → validated config → Spring properties
  domain/                     records, DomainException, LoanService + LoanStore   ← no Spring imports
  data/                       @Repository classes on JdbcClient
  http/                       controllers, Body (field-by-field validation), Problems (@RestControllerAdvice)
src/test/java/…/LoanServiceTest.java   JUnit 5, no Spring context
```

```bash
make test
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise make run
SHELFWISE_URL=http://localhost:3000 npm --prefix ../../conformance test
```

Things to notice (book chapters in brackets):

- **The domain has no annotations.** `LoanService` is built in one `@Bean` method, so its tests need
  only `new` and a fake store [Ch. 28].
- **`spring.threads.virtual.enabled=true`**: blocking JDBC on Java 21 virtual threads — the simple
  model without the thread-pool ceiling [Ch. 24].
- **Raw `String` bodies** parsed with Jackson's tree model let us reject unknown fields and tell
  absent from null; `@Valid` DTOs can't do the latter [Ch. 13].
- **Constraint names** come out of `PSQLException.getServerErrorMessage()` beneath Spring's
  `DataAccessException` translation [Ch. 21].
- Spring reads `DATABASE_URL` only as a JDBC URL, so `AppConfig` converts the `postgres://` form.
