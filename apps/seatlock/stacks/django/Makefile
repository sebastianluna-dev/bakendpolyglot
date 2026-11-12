# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
# Settings need these at import time; the unit tests never connect to them.
TEST_ENV = DATABASE_URL=$${DATABASE_URL:-postgres://unused@localhost/unused} OIDC_ISSUER=$${OIDC_ISSUER:-http://localhost:8081/realms/seatlock} \
           PAYMENTS_URL=$${PAYMENTS_URL:-http://localhost:4000} PAYMENT_WEBHOOK_SECRET=$${PAYMENT_WEBHOOK_SECRET:-whsec_test}
.PHONY: install lint build test run
install: ; uv sync --frozen
lint: install ; uv run ruff check .
build: install
test: install ; $(TEST_ENV) uv run pytest -q
run: install ; uv run gunicorn seatlock.wsgi
