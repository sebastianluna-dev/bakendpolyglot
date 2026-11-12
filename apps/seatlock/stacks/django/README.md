# SeatLock — Django + Django REST Framework

Django 6 ORM over the shared schema (`managed = False`), DRF for the HTTP layer, PyJWT for tokens,
Redis through Django's cache framework, gunicorn with threads.

```
seatlock/settings.py        env → settings, validated at import
ticketing/auth.py           BearerAuthentication (JWKS, RS256, iss, aud) + role permissions
ticketing/services.py       events, holds, checkout, payment events, reaper   ← the rules
ticketing/idempotency.py    Idempotency-Key storage (Chapter 24)
ticketing/webhooks.py       raw-body HMAC verification, outside DRF
ticketing/reaper.py         background reaper thread (one per gunicorn worker)
ticketing/management/commands/release_expired_holds.py   the same reaper for cron
```

```bash
make test                                       # no database needed
DATABASE_URL=… OIDC_ISSUER=http://localhost:8081/realms/seatlock PAYMENTS_URL=http://localhost:4000 \
  PAYMENT_WEBHOOK_SECRET=whsec_dev make run
```

Things to notice (book chapters in brackets):

- **The hold** is `select_for_update()` on the requested seats in id order (no deadlocks between
  overlapping holds), then one conditional `.update()` whose row count decides [Ch. 23].
- **Checkout commits before calling the provider**, and a retry resumes the same order, so an outage
  between the two steps never strands seats or creates a second intent [Ch. 27].
- **Webhooks** are a plain Django view: DRF would parse the body before the signature check [Ch. 24].
- **Ownership** is in the query (`filter(id=…, user_id=…)`): another user's hold is a 404 [Ch. 21].
- **Availability is never cached**; only the event and the seat layout are [Ch. 25].
