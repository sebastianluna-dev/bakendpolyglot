"""Every error leaves as application/problem+json (RFC 9457, Chapter 12)."""

import logging

from django.http import Http404, JsonResponse
from rest_framework import exceptions
from rest_framework.response import Response

from .errors import DomainError

log = logging.getLogger("shelfwise")
TYPE_BASE = "https://shelfwise.dev/problems/"
PROBLEM = "application/problem+json"


def body(e: DomainError, instance: str) -> dict:
    return {"type": TYPE_BASE + e.code, "title": e.title, "status": e.status, "detail": e.detail,
            "instance": instance, **e.extensions}


def flatten(detail, prefix: str = "") -> dict[str, list[str]]:
    """DRF error details (nested dicts and lists) → {"field": ["message", …]}."""
    out: dict[str, list[str]] = {}
    if isinstance(detail, dict):
        for key, value in detail.items():
            name = "body" if key == "non_field_errors" else f"{prefix}{key}"
            for k, v in flatten(value, "" if name == "body" else f"{name}.").items():
                out.setdefault(k if k else name, []).extend(v)
    elif isinstance(detail, list) and all(not isinstance(d, (dict, list)) for d in detail):
        out[prefix.rstrip(".")] = [str(d) for d in detail]
    elif isinstance(detail, list):
        for item in detail:
            for k, v in flatten(item, prefix).items():
                out.setdefault(k, []).extend(v)
    else:
        out[prefix.rstrip(".")] = [str(detail)]
    return out


def to_domain(exc: Exception) -> DomainError | None:
    if isinstance(exc, DomainError):
        return exc
    if isinstance(exc, exceptions.ValidationError):
        return DomainError("validation", 422, "Request is invalid", "One or more fields are invalid.",
                           errors=flatten(exc.detail))
    if isinstance(exc, exceptions.ParseError):
        return DomainError("invalid-body", 400, "Malformed JSON", "The request body is not valid JSON.")
    if isinstance(exc, (Http404, exceptions.NotFound)):
        return DomainError("not-found", 404, "Resource not found", "No such resource.")
    if isinstance(exc, exceptions.APIException):
        return DomainError("http-error", exc.status_code, str(exc.detail), str(exc.detail))
    return None


def exception_handler(exc: Exception, context) -> Response:
    request = context["request"]
    e = to_domain(exc)
    if e is None:
        log.exception("unhandled error")
        e = DomainError("internal", 500, "Internal error",
                        f"Something went wrong. Quote request id {request.META.get('HTTP_X_REQUEST_ID', '')}.")
    return Response(body(e, request.path), status=e.status, content_type=PROBLEM)


def not_found_view(request, exception=None):
    e = DomainError("not-found", 404, "Resource not found", "No such resource.")
    return JsonResponse(body(e, request.path), status=404, content_type=PROBLEM)
