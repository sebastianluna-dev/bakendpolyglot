import os

# Settings read these at import time; the unit tests never connect to any of them.
os.environ.setdefault("DATABASE_URL", "postgres://unused:unused@localhost:5432/unused")
os.environ.setdefault("OIDC_ISSUER", "http://localhost:8081/realms/seatlock")
os.environ.setdefault("PAYMENTS_URL", "http://localhost:4000")
os.environ.setdefault("PAYMENT_WEBHOOK_SECRET", "whsec_test")
