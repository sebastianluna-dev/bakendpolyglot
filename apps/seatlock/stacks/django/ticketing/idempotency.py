"""Idempotency keys (Chapter 24), stored in Postgres with the same transaction guarantees as the data."""

import hashlib
from collections.abc import Callable

from django.db import IntegrityError, transaction

from .errors import DomainError, idempotency_in_progress, idempotency_key_reuse
from .models import IdempotencyKey
from .problems import body as problem_body


def run_once(user_id: str, key: str, fingerprint: str, instance: str,
             operation: Callable[[], tuple[int, dict]]) -> tuple[int, dict, bool]:
    """Returns (status, body, replayed). 4xx outcomes are stored like successes; 5xx are not, so a
    retry after an outage runs the operation again."""
    request_hash = hashlib.sha256(fingerprint.encode()).hexdigest()
    try:
        with transaction.atomic():
            IdempotencyKey.objects.create(user_id=user_id, key=key, request_hash=request_hash)
    except IntegrityError:
        stored = IdempotencyKey.objects.get(user_id=user_id, key=key)
        if stored.request_hash != request_hash:
            raise idempotency_key_reuse() from None
        if stored.status is None:
            raise idempotency_in_progress() from None
        return stored.status, stored.response, True

    try:
        status, result = operation()
    except DomainError as e:
        if e.status >= 500:
            IdempotencyKey.objects.filter(user_id=user_id, key=key).delete()
            raise
        status, result = e.status, problem_body(e, instance)
    except BaseException:
        IdempotencyKey.objects.filter(user_id=user_id, key=key).delete()
        raise
    IdempotencyKey.objects.filter(user_id=user_id, key=key).update(status=status, response=result)
    return status, result, False
