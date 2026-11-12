"""The loan rules, in plain Python: no Django, no SQL (Chapter 18). Synchronous, like the rest of this WSGI stack."""

from collections.abc import Callable
from dataclasses import dataclass
from datetime import UTC, datetime, timedelta
from typing import Any, Protocol

from .errors import copy_on_loan, loan_limit, not_found

Loan = dict[str, Any]


class LoanStore(Protocol):
    """What the service needs from storage. The Postgres adapter satisfies it structurally; tests use a fake."""

    def member_exists(self, member_id: int) -> bool: ...
    def copy_exists(self, copy_id: int) -> bool: ...
    def copy_has_open_loan(self, copy_id: int) -> bool: ...
    def count_open_loans(self, member_id: int) -> int: ...
    def insert_loan(self, copy_id: int, member_id: int, loaned_at: datetime, due_at: datetime) -> Loan: ...
    def return_loan(self, loan_id: int, returned_at: datetime) -> Loan: ...


@dataclass(frozen=True)
class LoanRules:
    loan_days: int
    max_loans_per_member: int


def utc_now() -> datetime:
    return datetime.now(UTC)


class LoanService:
    def __init__(self, store: LoanStore, rules: LoanRules, now: Callable[[], datetime] = utc_now) -> None:
        self._store, self._rules, self._now = store, rules, now

    def lend(self, copy_id: int, member_id: int) -> Loan:
        if not self._store.copy_exists(copy_id):
            raise not_found("Copy", copy_id)
        if not self._store.member_exists(member_id):
            raise not_found("Member", member_id)
        # Friendly checks first; the partial unique index still settles races (Chapter 23).
        if self._store.copy_has_open_loan(copy_id):
            raise copy_on_loan(copy_id)
        if self._store.count_open_loans(member_id) >= self._rules.max_loans_per_member:
            raise loan_limit(member_id, self._rules.max_loans_per_member)

        loaned_at = self._now()
        return self._store.insert_loan(copy_id, member_id, loaned_at, loaned_at + timedelta(days=self._rules.loan_days))

    def return_loan(self, loan_id: int) -> Loan:
        return self._store.return_loan(loan_id, self._now())
