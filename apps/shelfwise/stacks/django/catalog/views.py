"""The handler layer: bind, validate, call a repository or service, map to HTTP (Chapter 18)."""

import re
from typing import Any

from django.conf import settings
from django.http import HttpResponse, JsonResponse
from rest_framework.request import Request
from rest_framework.response import Response
from rest_framework.views import APIView

from . import repos
from .cursor import decode_cursor, encode_cursor
from .errors import invalid_id, not_found
from .loans import LoanRules, LoanService
from .serializers import (
    CreateBookSerializer,
    CreateCopySerializer,
    CreateLoanSerializer,
    CreateMemberSerializer,
    ListQuerySerializer,
    UpdateBookSerializer,
)

loan_store = repos.LoanRepository()
loan_service = LoanService(loan_store, LoanRules(settings.SHELFWISE["LOAN_DAYS"], settings.SHELFWISE["MAX_LOANS_PER_MEMBER"]))


def valid(serializer_class, data) -> dict[str, Any]:
    s = serializer_class(data=data)
    s.is_valid(raise_exception=True)   # ValidationError → 422 problem in problems.exception_handler
    return s.validated_data


def parse_id(raw: str) -> int:
    if not raw.isascii() or not raw.isdigit() or not 1 <= int(raw) <= 2**53:
        raise invalid_id(raw)
    return int(raw)


def etag_of(book: dict[str, Any]) -> str:
    return f'"book-{book["id"]}-v{book["version"]}"'


def expected_version(header: str | None, book_id: int) -> int | None:
    if header is None:
        return None
    m = re.fullmatch(r'"book-(\d+)-v(\d+)"', header.strip())
    return int(m.group(2)) if m and int(m.group(1)) == book_id else -1


def page(items: list[dict[str, Any]], limit: int, cursor_of) -> dict[str, Any]:
    shown = items[:limit]
    return {"items": shown, "nextCursor": cursor_of(shown[-1]) if len(items) > limit and shown else None, "limit": limit}


def health(_request):
    return JsonResponse({"status": "ok"})


class BookListCreate(APIView):
    def get(self, request: Request) -> Response:
        q = valid(ListQuerySerializer, request.query_params)
        after = decode_cursor(q["after"], {"title": str, "id": int}) if "after" in q else None
        rows = repos.search_books((q.get("q") or "").strip() or None, after, q["limit"] + 1)
        return Response(page([repos.book_dict(b) for b in rows], q["limit"],
                             lambda b: encode_cursor({"title": b["title"], "id": b["id"]})))

    def post(self, request: Request) -> Response:
        data = valid(CreateBookSerializer, request.data)
        book = repos.book_dict(repos.create_book(data["isbn"], data["title"], data["author"], data["year"]))
        return Response(book, status=201, headers={"Location": f"/books/{book['id']}", "ETag": etag_of(book)})


class BookDetail(APIView):
    def get(self, request: Request, pk: str) -> Response | HttpResponse:
        book_id = parse_id(pk)
        found = repos.find_book(book_id)
        if found is None:
            raise not_found("Book", book_id)
        book = repos.book_dict(found)
        if request.headers.get("If-None-Match") == etag_of(book):
            return HttpResponse(status=304, headers={"ETag": etag_of(book)})
        return Response(book, headers={"ETag": etag_of(book)})

    def patch(self, request: Request, pk: str) -> Response:
        book_id = parse_id(pk)
        changes = valid(UpdateBookSerializer, request.data)
        updated = repos.update_book(book_id, dict(changes), expected_version(request.headers.get("If-Match"), book_id))
        book = repos.book_dict(updated)
        return Response(book, headers={"ETag": etag_of(book)})


class CopyCreate(APIView):
    def post(self, request: Request, pk: str) -> Response:
        book_id = parse_id(pk)
        data = valid(CreateCopySerializer, request.data)
        copy = repos.add_copy(book_id, data["barcode"])
        return Response({"id": copy.id, "bookId": copy.book_id, "barcode": copy.barcode, "status": copy.status},
                        status=201, headers={"Location": f"/copies/{copy.id}"})


class MemberCreate(APIView):
    def post(self, request: Request) -> Response:
        data = valid(CreateMemberSerializer, request.data)
        member = repos.create_member(data["email"], data["name"])
        return Response({"id": member.id, "email": member.email, "name": member.name},
                        status=201, headers={"Location": f"/members/{member.id}"})


class MemberLoans(APIView):
    def get(self, request: Request, pk: str) -> Response:
        member_id = parse_id(pk)
        q = valid(ListQuerySerializer, request.query_params)
        if not loan_store.member_exists(member_id):
            raise not_found("Member", member_id)
        before = decode_cursor(q["after"], {"id": int})["id"] if "after" in q else None
        rows = loan_store.for_member(member_id, q["status"] == "open", before, q["limit"] + 1)
        return Response(page(rows, q["limit"], lambda loan: encode_cursor({"id": loan["id"]})))


class LoanCreate(APIView):
    def post(self, request: Request) -> Response:
        data = valid(CreateLoanSerializer, request.data)
        loan = loan_service.lend(data["copyId"], data["memberId"])
        return Response(loan, status=201, headers={"Location": f"/loans/{loan['id']}"})


class LoanReturn(APIView):
    def post(self, request: Request, pk: str) -> Response:
        return Response(loan_service.return_loan(parse_id(pk)))
