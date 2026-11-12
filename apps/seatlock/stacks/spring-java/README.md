# SeatLock — Spring Boot (Java 21)

Spring Boot 3.5: Spring MVC on virtual threads, Spring Security as an OAuth2 resource server, Spring
Data JPA (Hibernate 6) for entities and the claim, `JdbcClient` for the few statements that are
clearer as SQL, and Spring Cache on Redis.

```
SeatLockApplication.java     entry point, Clock, typed Redis caches, cache error handler
AppConfig.java               env vars → validated config → Spring properties
data/                        entities and Spring Data repositories (the claim is a @Modifying query)
service/                     EventService (@Cacheable), SeatClaims + HoldService, CheckoutTx +
                             CheckoutService, Idempotency, PaymentsClient, PaymentEvents, Reaper
http/SecurityConfig.java     JwtDecoder (JWKS, RS256, issuer, audience), realm roles → ROLE_*, route rules
http/Controllers.java        every endpoint; raw String/byte[] bodies
```

Things to notice (book chapters in brackets):

- **Self-invocation bypasses `@Transactional`**, so the claim lives in its own bean (`SeatClaims`)
  and checkout's database half in `CheckoutTx`; the provider call happens between proxies [Ch. 23].
- **`@Lock(PESSIMISTIC_WRITE)`** on repository queries is Spring Data's `SELECT … FOR UPDATE`.
- **"Order" is a JPQL keyword**, hence the `SeatOrder` entity name over the `orders` table.
- **Roles are checked in the filter chain**, ownership in the queries; 401/403 bodies come from the
  entry point and access-denied handler, which run before MVC [Ch. 21].
- **Typed Redis serializers** per cache: the generic serializer can't round-trip records [Ch. 25].
