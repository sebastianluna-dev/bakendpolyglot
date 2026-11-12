"""The loan rules, in plain Python: no FastAPI, no SQL (Chapter 18)."""

from collections.abc import Callable
from dataclasses import dataclass
from datetime import UTC, datetime, timedelta
from typing import Any, Protocol

from .errors import copy_on_loan, loan_limit, not_found

Loan = dict[str, Any]


class LoanStore(Protocol):
    """What the service needs from storage. The Postgres adapter satisfies it structurally; tests use a fake."""

    async def member_exists(self, member_id: int) -> bool: ...
    async def copy_exists(self, copy_id: int) -> bool: ...
    async def copy_has_open_loan(self, copy_id: int) -> bool: ...
    async def count_open_loans(self, member_id: int) -> int: ...
    async def insert_loan(self, copy_id: int, member_id: int, loaned_at: datetime, due_at: datetime) -> Loan: ...
    async def return_loan(self, loan_id: int, returned_at: datetime) -> Loan: ...


@dataclass(frozen=True)
class LoanRules:
    loan_days: int
    max_loans_per_member: int


def utc_now() -> datetime:
    return datetime.now(UTC)


class LoanService:
    def __init__(self, store: LoanStore, rules: LoanRules, now: Callable[[], datetime] = utc_now) -> None:
        self._store, self._rules, self._now = store, rules, now

    async def lend(self, copy_id: int, member_id: int) -> Loan:
        if not await self._store.copy_exists(copy_id):
            raise not_found("Copy", copy_id)
        if not await self._store.member_exists(member_id):
            raise not_found("Member", member_id)
        # Friendly checks first; the partial unique index still settles races (Chapter 23).
        if await self._store.copy_has_open_loan(copy_id):
            raise copy_on_loan(copy_id)
        if await self._store.count_open_loans(member_id) >= self._rules.max_loans_per_member:
            raise loan_limit(member_id, self._rules.max_loans_per_member)

        loaned_at = self._now()
        return await self._store.insert_loan(copy_id, member_id, loaned_at, loaned_at + timedelta(days=self._rules.loan_days))

    async def return_loan(self, loan_id: int) -> Loan:
        return await self._store.return_loan(loan_id, self._now())
