"""Composition root (Chapter 28): settings, pool, repositories, service, app."""

import logging
import time
import uuid
from contextlib import asynccontextmanager
from types import SimpleNamespace

import asyncpg
from fastapi import FastAPI, Request

from .config import Settings
from .db.repos import BookRepository, CopyRepository, LoanRepository, MemberRepository
from .domain.loans import LoanRules, LoanService
from .http import problems
from .http.routes import router

log = logging.getLogger("shelfwise")


def create_app(settings: Settings | None = None) -> FastAPI:
    settings = settings or Settings()   # raises at startup if configuration is invalid (Chapter 16)

    @asynccontextmanager
    async def lifespan(app: FastAPI):
        pool = await asyncpg.create_pool(settings.database_url, min_size=1, max_size=10)
        loans = LoanRepository(pool)
        app.state.deps = SimpleNamespace(
            books=BookRepository(pool), copies=CopyRepository(pool), members=MemberRepository(pool), loans=loans,
            loan_service=LoanService(loans, LoanRules(settings.loan_days, settings.max_loans_per_member)),
        )
        yield
        await pool.close()   # graceful shutdown (Chapter 40)

    app = FastAPI(title="Shelfwise", lifespan=lifespan)
    problems.install(app)

    @app.middleware("http")
    async def request_id(request: Request, call_next):   # Chapter 11
        rid = request.headers.get("x-request-id") or str(uuid.uuid4())
        started = time.perf_counter()
        response = await call_next(request)
        response.headers["x-request-id"] = rid
        log.info("request", extra={"request_id": rid, "method": request.method, "path": request.url.path,
                                   "status": response.status_code, "ms": round((time.perf_counter() - started) * 1000)})
        return response

    app.include_router(router)
    return app
