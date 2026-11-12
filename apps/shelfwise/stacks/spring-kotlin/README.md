# Shelfwise — Spring Boot (Kotlin)

The same application as `../spring-java`, written the way Kotlin teams write Spring: data classes,
null-safe types, extension functions, top-level functions, and the Gradle Kotlin DSL. Spring Boot 3.5,
Spring MVC on virtual threads, `JdbcClient`, pgJDBC.

```
src/main/kotlin/dev/shelfwise/
  Application.kt        AppConfig, beans, main (with a self health check)
  domain/Domain.kt      data classes, DomainException, LoanService + LoanStore   ← no Spring imports
  data/Repositories.kt  @Repository classes on JdbcClient + a few extension functions
  http/                 Controllers, Body (field-by-field validation), Http (helpers, cursors), Web (filter, problems)
src/test/kotlin/…/LoanServiceTest.kt   kotlin.test on JUnit 5, no Spring context
```

```bash
make test
DATABASE_URL=postgres://app:app@localhost:5432/shelfwise make run
SHELFWISE_URL=http://localhost:3000 npm --prefix ../../conformance test
```

Put this next to the Java stack and compare (book chapters in brackets):

- **`String?` vs `String`**: absent PATCH fields are `null` in the type system, not by convention [Ch. 9].
- **`kotlin("plugin.spring")`** opens the classes Spring must proxy; Kotlin classes are `final` by default [Ch. 9].
- **`-Xjsr305=strict`** turns Spring's `@Nullable` annotations into real Kotlin nullability.
- **Extension functions** (`DataAccessException.violates`, `ObjectMapper.decodeCursor`) replace the
  Java version's static helper classes.
- **Backtick test names** read as sentences in the test report.
