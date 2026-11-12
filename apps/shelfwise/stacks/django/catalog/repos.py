"""Data access with the Django ORM. Database errors become domain errors here, nowhere else."""

from datetime import datetime
from typing import Any

from django.db import IntegrityError, transaction
from django.db.models import F, Q

from .errors import (
    already_returned,
    copy_on_loan,
    duplicate_barcode,
    duplicate_email,
    duplicate_isbn,
    not_found,
    version_mismatch,
)
from .models import Book, Copy, Loan, Member


def constraint_of(e: IntegrityError) -> str | None:
    """psycopg 3 exposes the violated constraint on the original error, which Django keeps as __cause__."""
    diag = getattr(e.__cause__, "diag", None)
    return getattr(diag, "constraint_name", None)


def book_dict(b: Book) -> dict[str, Any]:
    return {"id": b.id, "isbn": b.isbn, "title": b.title, "author": b.author, "year": b.year, "version": b.version}


def loan_dict(loan: Loan) -> dict[str, Any]:
    return {"id": loan.id, "copyId": loan.copy_id, "memberId": loan.member_id, "bookId": loan.copy.book_id,
            "title": loan.copy.book.title, "barcode": loan.copy.barcode, "loanedAt": loan.loaned_at,
            "dueAt": loan.due_at, "returnedAt": loan.returned_at}


def create_book(isbn: str, title: str, author: str, year: int | None) -> Book:
    try:
        with transaction.atomic():
            return Book.objects.create(isbn=isbn, title=title, author=author, year=year)
    except IntegrityError as e:
        if constraint_of(e) == "books_isbn_key":
            raise duplicate_isbn(isbn) from e
        raise


def find_book(book_id: int) -> Book | None:
    return Book.objects.filter(id=book_id).first()


def update_book(book_id: int, changes: dict[str, Any], expected_version: int | None) -> Book:
    rows = Book.objects.filter(id=book_id)
    if expected_version is not None:
        rows = rows.filter(version=expected_version)
    if rows.update(**changes, version=F("version") + 1) == 1:   # one conditional UPDATE (Chapter 23)
        return Book.objects.get(id=book_id)
    if Book.objects.filter(id=book_id).exists():
        raise version_mismatch(book_id)
    raise not_found("Book", book_id)


def search_books(q: str | None, after: dict[str, Any] | None, limit: int) -> list[Book]:
    books = Book.objects.order_by("title", "id")
    if q:
        books = books.filter(Q(title__icontains=q) | Q(author__icontains=q))
    if after:   # keyset: (title, id) > (after.title, after.id)
        books = books.filter(Q(title__gt=after["title"]) | Q(title=after["title"], id__gt=after["id"]))
    return list(books[:limit])


def add_copy(book_id: int, barcode: str) -> Copy:
    if not Book.objects.filter(id=book_id).exists():
        raise not_found("Book", book_id)
    try:
        with transaction.atomic():
            return Copy.objects.create(book_id=book_id, barcode=barcode)
    except IntegrityError as e:
        if constraint_of(e) == "copies_barcode_key":
            raise duplicate_barcode(barcode) from e
        raise


def create_member(email: str, name: str) -> Member:
    try:
        with transaction.atomic():
            return Member.objects.create(email=email, name=name)
    except IntegrityError as e:
        if constraint_of(e) == "members_email_key":
            raise duplicate_email(email) from e
        raise


class LoanRepository:
    """Satisfies loans.LoanStore."""

    def member_exists(self, member_id: int) -> bool:
        return Member.objects.filter(id=member_id).exists()

    def copy_exists(self, copy_id: int) -> bool:
        return Copy.objects.filter(id=copy_id).exists()

    def copy_has_open_loan(self, copy_id: int) -> bool:
        return Loan.objects.filter(copy_id=copy_id, returned_at__isnull=True).exists()

    def count_open_loans(self, member_id: int) -> int:
        return Loan.objects.filter(member_id=member_id, returned_at__isnull=True).count()

    def insert_loan(self, copy_id: int, member_id: int, loaned_at: datetime, due_at: datetime) -> dict[str, Any]:
        try:
            with transaction.atomic():
                loan = Loan.objects.create(copy_id=copy_id, member_id=member_id, loaned_at=loaned_at, due_at=due_at)
        except IntegrityError as e:
            if constraint_of(e) == "one_open_loan_per_copy":
                raise copy_on_loan(copy_id) from e
            raise
        return self.find(loan.id)  # type: ignore[return-value]

    def return_loan(self, loan_id: int, returned_at: datetime) -> dict[str, Any]:
        updated = Loan.objects.filter(id=loan_id, returned_at__isnull=True).update(returned_at=returned_at)
        loan = self.find(loan_id)
        if loan is None:
            raise not_found("Loan", loan_id)
        if updated == 0:
            raise already_returned(loan_id)
        return loan

    def find(self, loan_id: int) -> dict[str, Any] | None:
        loan = Loan.objects.select_related("copy__book").filter(id=loan_id).first()   # one query, no N+1
        return loan_dict(loan) if loan else None

    def for_member(self, member_id: int, open_only: bool, before_id: int | None, limit: int) -> list[dict[str, Any]]:
        loans = Loan.objects.select_related("copy__book").filter(member_id=member_id).order_by("-id")
        if open_only:
            loans = loans.filter(returned_at__isnull=True)
        if before_id is not None:
            loans = loans.filter(id__lt=before_id)
        return [loan_dict(loan) for loan in loans[:limit]]
