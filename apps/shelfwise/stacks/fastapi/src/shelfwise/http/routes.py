"""The handler layer: bind, validate, call a repository or service, map to HTTP (Chapter 18)."""

import re
from typing import Annotated, Any, Literal

from fastapi import APIRouter, Header, Query, Request, Response

from ..domain.cursor import decode_cursor, encode_cursor
from ..domain.errors import invalid_id, not_found
from .models import (
    CreateBookRequest,
    CreateCopyRequest,
    CreateLoanRequest,
    CreateMemberRequest,
    UpdateBookRequest,
)

router = APIRouter()
Limit = Annotated[int, Query(ge=1, le=100)]


def deps(request: Request) -> Any:
    return request.app.state.deps


def parse_id(raw: str) -> int:
    if not raw.isascii() or not raw.isdigit() or int(raw) < 1 or int(raw) > 2**53:
        raise invalid_id(raw)
    return int(raw)


def etag_of(book: dict[str, Any]) -> str:
    return f'"book-{book["id"]}-v{book["version"]}"'


def expected_version(header: str | None, book_id: int) -> int | None:
    """If-Match: "book-{id}-v{n}" → n. A header that names another book or version never matches."""
    if header is None:
        return None
    m = re.fullmatch(r'"book-(\d+)-v(\d+)"', header.strip())
    return int(m.group(2)) if m and int(m.group(1)) == book_id else -1


def page(rows: list[dict[str, Any]], limit: int, cursor_of) -> dict[str, Any]:
    items = rows[:limit]
    return {"items": items, "nextCursor": cursor_of(items[-1]) if len(rows) > limit and items else None, "limit": limit}


@router.get("/health")
async def health() -> dict[str, str]:
    return {"status": "ok"}


@router.get("/books")
async def list_books(request: Request, q: str | None = None, after: str | None = None, limit: Limit = 20):
    q = q.strip() if q and q.strip() else None
    cursor = decode_cursor(after, {"title": str, "id": int}) if after else None
    rows = await deps(request).books.search(q, cursor, limit + 1)   # one extra row tells us if there is a next page
    return page(rows, limit, lambda b: encode_cursor({"title": b["title"], "id": b["id"]}))


@router.post("/books", status_code=201)
async def create_book(request: Request, body: CreateBookRequest, response: Response):
    book = await deps(request).books.create(body.isbn, body.title, body.author, body.year)
    response.headers["Location"] = f"/books/{book['id']}"
    response.headers["ETag"] = etag_of(book)
    return book


@router.get("/books/{book_id}")
async def get_book(request: Request, book_id: str, response: Response,
                   if_none_match: Annotated[str | None, Header()] = None):
    bid = parse_id(book_id)
    book = await deps(request).books.find(bid)
    if book is None:
        raise not_found("Book", bid)
    if if_none_match == etag_of(book):
        return Response(status_code=304, headers={"ETag": etag_of(book)})
    response.headers["ETag"] = etag_of(book)
    return book


@router.patch("/books/{book_id}")
async def update_book(request: Request, book_id: str, body: UpdateBookRequest, response: Response,
                      if_match: Annotated[str | None, Header()] = None):
    bid = parse_id(book_id)
    book = await deps(request).books.update(bid, body.model_dump(exclude_unset=True), expected_version(if_match, bid))
    response.headers["ETag"] = etag_of(book)
    return book


@router.post("/books/{book_id}/copies", status_code=201)
async def create_copy(request: Request, book_id: str, body: CreateCopyRequest, response: Response):
    copy = await deps(request).copies.create(parse_id(book_id), body.barcode)
    response.headers["Location"] = f"/copies/{copy['id']}"
    return copy


@router.post("/members", status_code=201)
async def create_member(request: Request, body: CreateMemberRequest, response: Response):
    member = await deps(request).members.create(str(body.email), body.name)
    response.headers["Location"] = f"/members/{member['id']}"
    return member


@router.get("/members/{member_id}/loans")
async def member_loans(request: Request, member_id: str, status: Literal["open", "all"] = "all",
                       after: str | None = None, limit: Limit = 20):
    mid = parse_id(member_id)
    d = deps(request)
    if not await d.members.exists(mid):
        raise not_found("Member", mid)
    before = decode_cursor(after, {"id": int})["id"] if after else None
    rows = await d.loans.for_member(mid, status == "open", before, limit + 1)
    return page(rows, limit, lambda loan: encode_cursor({"id": loan["id"]}))


@router.post("/loans", status_code=201)
async def create_loan(request: Request, body: CreateLoanRequest, response: Response):
    loan = await deps(request).loan_service.lend(body.copyId, body.memberId)
    response.headers["Location"] = f"/loans/{loan['id']}"
    return loan


@router.post("/loans/{loan_id}/return")
async def return_loan(request: Request, loan_id: str):
    return await deps(request).loan_service.return_loan(parse_id(loan_id))
