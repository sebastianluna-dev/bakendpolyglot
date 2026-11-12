"""Postgres adapters (asyncpg). Database errors become domain errors here, nowhere else."""

from datetime import datetime
from typing import Any

import asyncpg

from ..domain.errors import (
    already_returned,
    copy_on_loan,
    duplicate_barcode,
    duplicate_email,
    duplicate_isbn,
    not_found,
    version_mismatch,
)

Row = dict[str, Any]
BOOK_COLUMNS = "id, isbn, title, author, year, version"

# One query shape for every loan we return: the loan plus its copy's barcode and book title.
SELECT_LOAN = """
  SELECT l.id, l.copy_id AS "copyId", l.member_id AS "memberId", c.book_id AS "bookId",
         b.title, c.barcode, l.loaned_at AS "loanedAt", l.due_at AS "dueAt", l.returned_at AS "returnedAt"
  FROM loans l
  JOIN copies c ON c.id = l.copy_id
  JOIN books  b ON b.id = c.book_id"""


def _row(record: asyncpg.Record | None) -> Row | None:
    return dict(record) if record is not None else None


class BookRepository:
    def __init__(self, pool: asyncpg.Pool) -> None:
        self._pool = pool

    async def create(self, isbn: str, title: str, author: str, year: int | None) -> Row:
        try:
            return dict(await self._pool.fetchrow(
                f"INSERT INTO books (isbn, title, author, year) VALUES ($1, $2, $3, $4) RETURNING {BOOK_COLUMNS}",
                isbn, title, author, year))
        except asyncpg.UniqueViolationError as e:
            if e.constraint_name == "books_isbn_key":
                raise duplicate_isbn(isbn) from e
            raise

    async def find(self, book_id: int) -> Row | None:
        return _row(await self._pool.fetchrow(f"SELECT {BOOK_COLUMNS} FROM books WHERE id = $1", book_id))

    async def update(self, book_id: int, changes: dict[str, Any], expected_version: int | None) -> Row:
        sets, params = [], [book_id, expected_version]
        for column, value in changes.items():          # keys come from the validated DTO only
            params.append(value)
            sets.append(f"{column} = ${len(params)}")
        row = await self._pool.fetchrow(
            f"""UPDATE books SET {', '.join(sets)}, version = version + 1
                WHERE id = $1 AND ($2::int IS NULL OR version = $2) RETURNING {BOOK_COLUMNS}""", *params)
        if row:
            return dict(row)
        if await self.find(book_id):
            raise version_mismatch(book_id)
        raise not_found("Book", book_id)

    async def search(self, q: str | None, after: Row | None, limit: int) -> list[Row]:
        pattern = None if q is None else "%" + q.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%"
        rows = await self._pool.fetch(
            f"""SELECT {BOOK_COLUMNS} FROM books
                WHERE ($1::text IS NULL OR title ILIKE $1 OR author ILIKE $1)
                  AND ($2::text IS NULL OR (title, id) > ($2::text, $3::bigint))
                ORDER BY title, id LIMIT $4""",
            pattern, after and after["title"], after and after["id"], limit)
        return [dict(r) for r in rows]


class CopyRepository:
    def __init__(self, pool: asyncpg.Pool) -> None:
        self._pool = pool

    async def create(self, book_id: int, barcode: str) -> Row:
        try:
            return dict(await self._pool.fetchrow(
                'INSERT INTO copies (book_id, barcode) VALUES ($1, $2) RETURNING id, book_id AS "bookId", barcode, status',
                book_id, barcode))
        except asyncpg.ForeignKeyViolationError as e:
            raise not_found("Book", book_id) from e
        except asyncpg.UniqueViolationError as e:
            if e.constraint_name == "copies_barcode_key":
                raise duplicate_barcode(barcode) from e
            raise


class MemberRepository:
    def __init__(self, pool: asyncpg.Pool) -> None:
        self._pool = pool

    async def create(self, email: str, name: str) -> Row:
        try:
            return dict(await self._pool.fetchrow(
                "INSERT INTO members (email, name) VALUES ($1, $2) RETURNING id, email, name", email, name))
        except asyncpg.UniqueViolationError as e:
            if e.constraint_name == "members_email_key":
                raise duplicate_email(email) from e
            raise

    async def exists(self, member_id: int) -> bool:
        return await self._pool.fetchval("SELECT EXISTS (SELECT 1 FROM members WHERE id = $1)", member_id)


class LoanRepository:
    """Satisfies domain.loans.LoanStore."""

    def __init__(self, pool: asyncpg.Pool) -> None:
        self._pool = pool

    async def member_exists(self, member_id: int) -> bool:
        return await self._pool.fetchval("SELECT EXISTS (SELECT 1 FROM members WHERE id = $1)", member_id)

    async def copy_exists(self, copy_id: int) -> bool:
        return await self._pool.fetchval("SELECT EXISTS (SELECT 1 FROM copies WHERE id = $1)", copy_id)

    async def copy_has_open_loan(self, copy_id: int) -> bool:
        return await self._pool.fetchval(
            "SELECT EXISTS (SELECT 1 FROM loans WHERE copy_id = $1 AND returned_at IS NULL)", copy_id)

    async def count_open_loans(self, member_id: int) -> int:
        return await self._pool.fetchval(
            "SELECT count(*) FROM loans WHERE member_id = $1 AND returned_at IS NULL", member_id)

    async def insert_loan(self, copy_id: int, member_id: int, loaned_at: datetime, due_at: datetime) -> Row:
        try:
            loan_id = await self._pool.fetchval(
                "INSERT INTO loans (copy_id, member_id, loaned_at, due_at) VALUES ($1, $2, $3, $4) RETURNING id",
                copy_id, member_id, loaned_at, due_at)
        except asyncpg.UniqueViolationError as e:
            if e.constraint_name == "one_open_loan_per_copy":
                raise copy_on_loan(copy_id) from e
            raise
        return await self.find(loan_id)  # type: ignore[return-value]

    async def return_loan(self, loan_id: int, returned_at: datetime) -> Row:
        status = await self._pool.execute(
            "UPDATE loans SET returned_at = $2 WHERE id = $1 AND returned_at IS NULL", loan_id, returned_at)
        loan = await self.find(loan_id)
        if loan is None:
            raise not_found("Loan", loan_id)
        if status == "UPDATE 0":
            raise already_returned(loan_id)
        return loan

    async def find(self, loan_id: int) -> Row | None:
        return _row(await self._pool.fetchrow(f"{SELECT_LOAN} WHERE l.id = $1", loan_id))

    async def for_member(self, member_id: int, open_only: bool, before_id: int | None, limit: int) -> list[Row]:
        rows = await self._pool.fetch(
            f"""{SELECT_LOAN}
                WHERE l.member_id = $1
                  AND (NOT $2::boolean OR l.returned_at IS NULL)
                  AND ($3::bigint IS NULL OR l.id < $3)
                ORDER BY l.id DESC LIMIT $4""",
            member_id, open_only, before_id, limit)
        return [dict(r) for r in rows]
