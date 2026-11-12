"""Every error leaves as application/problem+json (RFC 9457, Chapter 12)."""

import logging

from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
from starlette.exceptions import HTTPException as StarletteHTTPException

from ..domain.errors import DomainError

log = logging.getLogger("shelfwise")
TYPE_BASE = "https://shelfwise.dev/problems/"


def problem(request: Request, e: DomainError) -> JSONResponse:
    body = {"type": TYPE_BASE + e.code, "title": e.title, "status": e.status, "detail": e.detail,
            "instance": request.url.path, **e.extensions}
    return JSONResponse(body, status_code=e.status, media_type="application/problem+json")


def install(app: FastAPI) -> None:
    @app.exception_handler(DomainError)
    async def domain_error(request: Request, e: DomainError) -> JSONResponse:
        return problem(request, e)

    @app.exception_handler(RequestValidationError)
    async def validation_error(request: Request, e: RequestValidationError) -> JSONResponse:
        if any(err["type"] == "json_invalid" for err in e.errors()):
            return problem(request, DomainError("invalid-body", 400, "Malformed JSON", "The request body is not valid JSON."))
        errors: dict[str, list[str]] = {}
        for err in e.errors():
            field = ".".join(str(p) for p in err["loc"][1:]) or "body"
            message = "is not allowed" if err["type"] == "extra_forbidden" else err["msg"].removeprefix("Value error, ")
            errors.setdefault(field, []).append(message)
        return problem(request, DomainError("validation", 422, "Request is invalid", "One or more fields are invalid.",
                                            errors=errors))

    @app.exception_handler(StarletteHTTPException)
    async def http_error(request: Request, e: StarletteHTTPException) -> JSONResponse:
        code = "not-found" if e.status_code == 404 else "http-error"
        return problem(request, DomainError(code, e.status_code, str(e.detail), str(e.detail)))

    @app.exception_handler(Exception)
    async def unhandled(request: Request, e: Exception) -> JSONResponse:
        rid = request.headers.get("x-request-id", "")
        log.exception("unhandled error", extra={"request_id": rid})
        return problem(request, DomainError("internal", 500, "Internal error", f"Something went wrong. Quote request id {rid}."))
