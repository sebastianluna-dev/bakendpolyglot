"""Unit tests for the loan rules: no database, no HTTP, a fixed clock (Chapter 17)."""

import asyncio
from datetime import UTC, datetime

import pytest

from shelfwise.domain.errors import DomainError, copy_on_loan
from shelfwise.domain.loans import LoanRules, LoanService


class FakeStore:
    def __init__(self) -> None:
        self.open: dict[int, int] = {}   # copy_id → member_id
        self.copies, self.members = {1, 2, 3}, {10}

    async def member_exists(self, member_id: int) -> bool: return member_id in self.members
    async def copy_exists(self, copy_id: int) -> bool: return copy_id in self.copies
    async def copy_has_open_loan(self, copy_id: int) -> bool: return copy_id in self.open
    async def count_open_loans(self, member_id: int) -> int: return list(self.open.values()).count(member_id)

    async def insert_loan(self, copy_id, member_id, loaned_at, due_at):
        if copy_id in self.open:
            raise copy_on_loan(copy_id)
        self.open[copy_id] = member_id
        return {"id": len(self.open), "copyId": copy_id, "memberId": member_id, "loanedAt": loaned_at, "dueAt": due_at}

    async def return_loan(self, loan_id, returned_at): raise NotImplementedError


def jan31() -> datetime:
    return datetime(2026, 1, 31, 10, 0, tzinfo=UTC)


def service(store: FakeStore, max_loans: int = 5) -> LoanService:
    return LoanService(store, LoanRules(loan_days=21, max_loans_per_member=max_loans), now=jan31)


def test_due_date_crosses_month_end():
    loan = asyncio.run(service(FakeStore()).lend(1, 10))
    assert loan["dueAt"] == datetime(2026, 2, 21, 10, 0, tzinfo=UTC)


def test_refuses_copy_already_on_loan():
    store = FakeStore()
    store.open[1] = 99
    with pytest.raises(DomainError) as e:
        asyncio.run(service(store).lend(1, 10))
    assert e.value.code == "copy-on-loan"


def test_enforces_member_limit():
    store = FakeStore()
    svc = service(store, max_loans=2)
    asyncio.run(svc.lend(1, 10))
    asyncio.run(svc.lend(2, 10))
    with pytest.raises(DomainError) as e:
        asyncio.run(svc.lend(3, 10))
    assert (e.value.code, e.value.status) == ("loan-limit", 409)


def test_unknown_copy_and_member_are_not_found():
    svc = service(FakeStore())
    for copy_id, member_id in [(99, 10), (1, 99)]:
        with pytest.raises(DomainError) as e:
            asyncio.run(svc.lend(copy_id, member_id))
        assert e.value.code == "not-found"
