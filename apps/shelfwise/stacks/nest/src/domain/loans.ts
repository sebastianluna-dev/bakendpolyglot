// The loan rules live here, in plain TypeScript: no Express, no SQL (Chapter 18).
import { copyOnLoan, loanLimit, notFound } from './errors.js';
import type { Loan } from './types.js';

/** What the service needs from storage. The Postgres adapter implements it; tests use a fake. */
export interface LoanStore {
  memberExists(memberId: number): Promise<boolean>;
  copyExists(copyId: number): Promise<boolean>;
  countOpenLoans(memberId: number): Promise<number>;
  copyHasOpenLoan(copyId: number): Promise<boolean>;
  /** Must throw copyOnLoan() if the database rejects a second open loan for the copy. */
  insertLoan(copyId: number, memberId: number, loanedAt: Date, dueAt: Date): Promise<Loan>;
  returnLoan(loanId: number, returnedAt: Date): Promise<Loan>;
}

export interface LoanRules {
  loanDays: number;
  maxLoansPerMember: number;
}

const DAY_MS = 24 * 60 * 60 * 1000;

export class LoanService {
  constructor(
    private readonly store: LoanStore,
    private readonly rules: LoanRules,
    private readonly now: () => Date = () => new Date(),
  ) {}

  async lend(copyId: number, memberId: number): Promise<Loan> {
    if (!(await this.store.copyExists(copyId))) throw notFound('Copy', copyId);
    if (!(await this.store.memberExists(memberId))) throw notFound('Member', memberId);

    // Friendly checks first; the partial unique index still settles races (Chapter 23).
    if (await this.store.copyHasOpenLoan(copyId)) throw copyOnLoan(copyId);
    if ((await this.store.countOpenLoans(memberId)) >= this.rules.maxLoansPerMember) {
      throw loanLimit(memberId, this.rules.maxLoansPerMember);
    }

    const loanedAt = this.now();
    const dueAt = new Date(loanedAt.getTime() + this.rules.loanDays * DAY_MS);
    return this.store.insertLoan(copyId, memberId, loanedAt, dueAt);
  }

  returnLoan(loanId: number): Promise<Loan> {
    return this.store.returnLoan(loanId, this.now());
  }
}
