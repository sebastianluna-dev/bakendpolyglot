"""Django settings. Everything environment-specific comes from environment variables (Chapter 16)."""

import os
from urllib.parse import unquote, urlparse

from django.core.exceptions import ImproperlyConfigured


def env_int(name: str, default: int, lo: int, hi: int) -> int:
    raw = os.environ.get(name)
    value = default if not raw else int(raw)
    if not lo <= value <= hi:
        raise ImproperlyConfigured(f"{name} must be between {lo} and {hi}, got {value}")
    return value


def required(name: str) -> str:
    value = os.environ.get(name)
    if not value:
        raise ImproperlyConfigured(f"{name} is required")
    return value


_db = urlparse(required("DATABASE_URL"))
_issuer = required("OIDC_ISSUER").rstrip("/")

SECRET_KEY = os.environ.get("SECRET_KEY", "dev-only-not-secret")  # no sessions or signing in this API
DEBUG = False
ALLOWED_HOSTS = ["*"]
ROOT_URLCONF = "seatlock.urls"
WSGI_APPLICATION = "seatlock.wsgi.application"
USE_TZ = True
TIME_ZONE = "UTC"
APPEND_SLASH = False

INSTALLED_APPS = ["django.contrib.contenttypes", "django.contrib.auth", "rest_framework", "ticketing"]
MIDDLEWARE = ["ticketing.middleware.request_id", "django.middleware.common.CommonMiddleware"]

DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.postgresql",
        "NAME": _db.path.lstrip("/"),
        "USER": unquote(_db.username or ""),
        "PASSWORD": unquote(_db.password or ""),
        "HOST": _db.hostname,
        "PORT": _db.port or 5432,
        "CONN_MAX_AGE": 60,
        "CONN_HEALTH_CHECKS": True,
    }
}

# Redis is the shared cache (Chapter 25). Without REDIS_URL nothing is cached, which is always correct.
CACHES = {
    "default": (
        {"BACKEND": "django.core.cache.backends.redis.RedisCache", "LOCATION": os.environ["REDIS_URL"]}
        if os.environ.get("REDIS_URL")
        else {"BACKEND": "django.core.cache.backends.dummy.DummyCache"}
    )
}

REST_FRAMEWORK = {
    "DEFAULT_AUTHENTICATION_CLASSES": ["ticketing.auth.BearerAuthentication"],
    "DEFAULT_PERMISSION_CLASSES": ["rest_framework.permissions.AllowAny"],
    "UNAUTHENTICATED_USER": None,
    "DEFAULT_RENDERER_CLASSES": ["rest_framework.renderers.JSONRenderer"],
    "DEFAULT_PARSER_CLASSES": ["rest_framework.parsers.JSONParser"],
    "EXCEPTION_HANDLER": "ticketing.problems.exception_handler",
}

SEATLOCK = {
    "OIDC_ISSUER": _issuer,
    "OIDC_JWKS_URL": os.environ.get("OIDC_JWKS_URL") or f"{_issuer}/protocol/openid-connect/certs",
    "OIDC_AUDIENCE": os.environ.get("OIDC_AUDIENCE", "seatlock-api"),
    "PAYMENTS_URL": required("PAYMENTS_URL").rstrip("/"),
    "PAYMENT_WEBHOOK_SECRET": required("PAYMENT_WEBHOOK_SECRET"),
    "HOLD_TTL_SECONDS": env_int("HOLD_TTL_SECONDS", 600, 1, 3600),
    "REAPER_INTERVAL_SECONDS": env_int("REAPER_INTERVAL_SECONDS", 60, 0, 3600),
    "MAX_SEATS_PER_HOLD": env_int("MAX_SEATS_PER_HOLD", 6, 1, 20),
}

LOGGING = {
    "version": 1,
    "handlers": {"console": {"class": "logging.StreamHandler"}},
    "root": {"handlers": ["console"], "level": os.environ.get("LOG_LEVEL", "INFO").upper()},
}
