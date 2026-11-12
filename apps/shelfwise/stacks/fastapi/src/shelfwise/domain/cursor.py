"""Opaque cursors: base64url-encoded JSON (Chapter 12)."""

import base64
import json
from typing import Any

from .errors import bad_cursor


def encode_cursor(value: dict[str, Any]) -> str:
    return base64.urlsafe_b64encode(json.dumps(value, separators=(",", ":")).encode()).decode().rstrip("=")


def decode_cursor(raw: str, required: dict[str, type]) -> dict[str, Any]:
    try:
        value = json.loads(base64.urlsafe_b64decode(raw + "=" * (-len(raw) % 4)))
    except ValueError:
        raise bad_cursor() from None
    if not isinstance(value, dict) or any(not isinstance(value.get(k), t) for k, t in required.items()):
        raise bad_cursor()
    return value
