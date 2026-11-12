// Unit tests for the loan rules: no database, no HTTP, a fixed clock (Chapter 17).
import { describe, expect, it } from 'vitest';
import { DomainError, copyOnLoan } from '../src/domain/errors.js';
import { LoanService, type LoanStore } from '../src/domain/loans.js';
import type { Loan } from '../src/domain/types.js';

class FakeStore implements LoanStore {
  open = new Map<number, number>(); // copyId → memberId
  copies = new Set([1, 2, 3]);
  members = new Set([10]);
  inserted: { copyId: number; dueAt: Date }[] = [];

  async memberExists(id: number) { return this.members.has(id); }
  async copyExists(id: number) { return this.copies.has(id); }
  async copyHasOpenLoan(id: number) { return this.open.has(id); }
  async countOpenLoans(memberId: number) { return [...this.open.values()].filter((m) => m === memberId).length; }
  async insertLoan(copyId: number, memberId: number, loanedAt: Date, dueAt: Date): Promise<Loan> {
    if (this.open.has(copyId)) throw copyOnLoan(copyId);
    this.open.set(copyId, memberId);
    this.inserted.push({ copyId, dueAt });
    return { id: this.inserted.length, copyId, memberId, bookId: 1, title: 'Dune', barcode: 'C-1', loanedAt, dueAt, returnedAt: null };
  }
  async returnLoan(): Promise<Loan> { throw new Error('not used'); }
}

const jan31 = () => new Date('2026-01-31T10:00:00Z');

describe('LoanService.lend', () => {
  it('sets the due date LOAN_DAYS after the loan, across month ends', async () => {
    const store = new FakeStore();
    const loan = await new LoanService(store, { loanDays: 21, maxLoansPerMember: 5 }, jan31).lend(1, 10);
    expect(loan.dueAt.toISOString()).toBe('2026-02-21T10:00:00.000Z');
  });

  it('refuses a copy already on loan', async () => {
    const store = new FakeStore();
    store.open.set(1, 99);
    await expect(new LoanService(store, { loanDays: 21, maxLoansPerMember: 5 }, jan31).lend(1, 10)).rejects.toMatchObject({ code: 'copy-on-loan' });
  });

  it('enforces the per-member limit', async () => {
    const store = new FakeStore();
    const service = new LoanService(store, { loanDays: 21, maxLoansPerMember: 2 }, jan31);
    await service.lend(1, 10);
    await service.lend(2, 10);
    await expect(service.lend(3, 10)).rejects.toMatchObject({ code: 'loan-limit', status: 409 });
  });

  it('answers not-found for unknown copies and members', async () => {
    const service = new LoanService(new FakeStore(), { loanDays: 21, maxLoansPerMember: 5 }, jan31);
    await expect(service.lend(99, 10)).rejects.toBeInstanceOf(DomainError);
    await expect(service.lend(1, 99)).rejects.toMatchObject({ code: 'not-found' });
  });
});
