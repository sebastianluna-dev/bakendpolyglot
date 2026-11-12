"""Domain errors. The HTTP layer maps each one to a Problem Details response (Chapter 12)."""

from typing import Any


class DomainError(Exception):
    def __init__(self, code: str, status: int, title: str, detail: str, **extensions: Any) -> None:
        super().__init__(detail)
        self.code, self.status, self.title, self.detail, self.extensions = code, status, title, detail, extensions


def not_found(what: str, id_: int | str) -> DomainError:
    return DomainError("not-found", 404, "Resource not found", f"{what} {id_} does not exist.")


def duplicate_isbn(isbn: str) -> DomainError:
    return DomainError("duplicate-isbn", 409, "ISBN already in the catalog", f"A book with ISBN {isbn} already exists.", isbn=isbn)


def duplicate_barcode(barcode: str) -> DomainError:
    return DomainError("duplicate-barcode", 409, "Barcode already in use", f"A copy with barcode {barcode} already exists.", barcode=barcode)


def duplicate_email(email: str) -> DomainError:
    return DomainError("duplicate-email", 409, "Email already registered", f"A member with email {email} already exists.")


def copy_on_loan(copy_id: int) -> DomainError:
    return DomainError("copy-on-loan", 409, "Copy is already on loan", f"Copy {copy_id} has an open loan.", copyId=copy_id)


def loan_limit(member_id: int, maximum: int) -> DomainError:
    return DomainError("loan-limit", 409, "Loan limit reached", f"Member {member_id} already has {maximum} open loans.",
                       memberId=member_id, max=maximum)


def already_returned(loan_id: int) -> DomainError:
    return DomainError("already-returned", 409, "Loan already returned", f"Loan {loan_id} was already returned.")


def version_mismatch(book_id: int) -> DomainError:
    return DomainError("version-mismatch", 412, "Book was changed by someone else",
                       f"Book {book_id} does not match the If-Match version.")


def bad_cursor() -> DomainError:
    return DomainError("bad-cursor", 400, "Invalid cursor", "The cursor is malformed or expired; start from the first page.")


def invalid_id(raw: str) -> DomainError:
    return DomainError("invalid-id", 400, "Invalid id", f'"{raw}" is not a valid id.')
