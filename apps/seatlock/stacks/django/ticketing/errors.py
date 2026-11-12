"""Domain errors. The HTTP layer maps each one to a Problem Details response (Chapter 12)."""

from typing import Any


class DomainError(Exception):
    def __init__(self, code: str, status: int, title: str, detail: str, **extensions: Any) -> None:
        super().__init__(detail)
        self.code, self.status, self.title, self.detail, self.extensions = code, status, title, detail, extensions


def not_found(what: str, id_: object) -> DomainError:
    return DomainError("not-found", 404, "Resource not found", f"{what} {id_} does not exist.")


def invalid_id(raw: str) -> DomainError:
    return DomainError("invalid-id", 400, "Invalid id", f'"{raw}" is not a valid id.')


def seats_unavailable(seat_ids: list[str]) -> DomainError:
    return DomainError("seats-unavailable", 409, "Seats are not available",
                       "One or more seats are held or sold. Nothing was held.", seatIds=seat_ids)


def hold_not_active(hold_id: object) -> DomainError:
    return DomainError("hold-not-active", 409, "Hold is not active", f"Hold {hold_id} was released or checked out.")


def hold_expired(hold_id: object) -> DomainError:
    return DomainError("hold-expired", 410, "Hold expired", f"Hold {hold_id} expired; its seats are back on sale.")


def idempotency_key_required() -> DomainError:
    return DomainError("idempotency-key-required", 400, "Idempotency-Key required",
                       "Send an Idempotency-Key header so the request can be retried safely.")


def idempotency_key_reuse() -> DomainError:
    return DomainError("idempotency-key-reuse", 422, "Idempotency-Key reused",
                       "This key was already used for a different request.")


def idempotency_in_progress() -> DomainError:
    return DomainError("idempotency-in-progress", 409, "Request in progress",
                       "A request with this Idempotency-Key is still running; retry shortly.")


def payment_provider_unavailable() -> DomainError:
    return DomainError("payment-provider-unavailable", 502, "Payment provider unavailable",
                       "The payment provider did not answer. Retry with the same Idempotency-Key.")


def invalid_signature() -> DomainError:
    return DomainError("invalid-signature", 401, "Invalid signature", "X-Signature does not match the body.")


def invalid_body() -> DomainError:
    return DomainError("invalid-body", 400, "Malformed body", "The request body is not a valid JSON document of the expected shape.")
