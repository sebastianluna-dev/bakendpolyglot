"""Django settings. Everything environment-specific comes from environment variables (Chapter 16)."""

import os
from urllib.parse import unquote, urlparse

from django.core.exceptions import ImproperlyConfigured


def env_int(name: str, default: int, lo: int, hi: int) -> int:
    raw = os.environ.get(name)
    value = default if raw is None else int(raw)
    if not lo <= value <= hi:
        raise ImproperlyConfigured(f"{name} must be between {lo} and {hi}, got {value}")
    return value


if not os.environ.get("DATABASE_URL"):
    raise ImproperlyConfigured("DATABASE_URL is required")
_db = urlparse(os.environ["DATABASE_URL"])

SECRET_KEY = os.environ.get("SECRET_KEY", "dev-only-not-secret")   # no sessions or signing in this API
DEBUG = False
ALLOWED_HOSTS = ["*"]
ROOT_URLCONF = "shelfwise.urls"
WSGI_APPLICATION = "shelfwise.wsgi.application"
USE_TZ = True
TIME_ZONE = "UTC"

INSTALLED_APPS = ["django.contrib.contenttypes", "django.contrib.auth", "rest_framework", "catalog"]
MIDDLEWARE = ["catalog.middleware.request_id", "django.middleware.common.CommonMiddleware"]
APPEND_SLASH = False

DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.postgresql",
        "NAME": _db.path.lstrip("/"),
        "USER": unquote(_db.username or ""),
        "PASSWORD": unquote(_db.password or ""),
        "HOST": _db.hostname,
        "PORT": _db.port or 5432,
        "CONN_MAX_AGE": 60,          # persistent connections (Chapter 15)
        "CONN_HEALTH_CHECKS": True,
    }
}

REST_FRAMEWORK = {
    "DEFAULT_AUTHENTICATION_CLASSES": [],
    "DEFAULT_PERMISSION_CLASSES": ["rest_framework.permissions.AllowAny"],
    "UNAUTHENTICATED_USER": None,
    "DEFAULT_RENDERER_CLASSES": ["rest_framework.renderers.JSONRenderer"],
    "DEFAULT_PARSER_CLASSES": ["rest_framework.parsers.JSONParser"],
    "EXCEPTION_HANDLER": "catalog.problems.exception_handler",
}

SHELFWISE = {
    "LOAN_DAYS": env_int("LOAN_DAYS", 21, 1, 60),
    "MAX_LOANS_PER_MEMBER": env_int("MAX_LOANS_PER_MEMBER", 5, 1, 50),
}

LOGGING = {
    "version": 1,
    "handlers": {"console": {"class": "logging.StreamHandler"}},
    "root": {"handlers": ["console"], "level": os.environ.get("LOG_LEVEL", "INFO")},
}
