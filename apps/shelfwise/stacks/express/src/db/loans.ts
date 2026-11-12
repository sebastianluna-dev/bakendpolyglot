import { alreadyReturned, copyOnLoan, notFound } from '../domain/errors.js';
import type { LoanStore } from '../domain/loans.js';
import type { Loan } from '../domain/types.js';
import { isUniqueViolation, type Db } from './pool.js';

// One query shape for every loan we return: the loan plus its copy’s barcode and book title.
const SELECT_LOAN = `
  SELECT l.id, l.copy_id AS "copyId", l.member_id AS "memberId", c.book_id AS "bookId",
         b.title, c.barcode, l.loaned_at AS "loanedAt", l.due_at AS "dueAt", l.returned_at AS "returnedAt"
  FROM loans l
  JOIN copies c ON c.id = l.copy_id
  JOIN books  b ON b.id = c.book_id`;

export class LoanRepository implements LoanStore {
  constructor(private readonly db: Db) {}

  async memberExists(memberId: number) {
    return (await this.db.query('SELECT 1 FROM members WHERE id = $1', [memberId])).rowCount === 1;
  }

  async copyExists(copyId: number) {
    return (await this.db.query('SELECT 1 FROM copies WHERE id = $1', [copyId])).rowCount === 1;
  }

  async copyHasOpenLoan(copyId: number) {
    return (await this.db.query('SELECT 1 FROM loans WHERE copy_id = $1 AND returned_at IS NULL', [copyId])).rowCount === 1;
  }

  async countOpenLoans(memberId: number) {
    const { rows } = await this.db.query<{ n: number }>(
      'SELECT count(*)::int AS n FROM loans WHERE member_id = $1 AND returned_at IS NULL',
      [memberId],
    );
    return rows[0]!.n;
  }

  async insertLoan(copyId: number, memberId: number, loanedAt: Date, dueAt: Date): Promise<Loan> {
    try {
      const { rows } = await this.db.query<{ id: number }>(
        'INSERT INTO loans (copy_id, member_id, loaned_at, due_at) VALUES ($1, $2, $3, $4) RETURNING id',
        [copyId, memberId, loanedAt, dueAt],
      );
      return (await this.find(rows[0]!.id))!;
    } catch (err) {
      if (isUniqueViolation(err, 'one_open_loan_per_copy')) throw copyOnLoan(copyId);
      throw err;
    }
  }

  async returnLoan(loanId: number, returnedAt: Date): Promise<Loan> {
    const { rowCount } = await this.db.query(
      'UPDATE loans SET returned_at = $2 WHERE id = $1 AND returned_at IS NULL',
      [loanId, returnedAt],
    );
    const loan = await this.find(loanId);
    if (!loan) throw notFound('Loan', loanId);
    if (rowCount === 0) throw alreadyReturned(loanId);
    return loan;
  }

  async find(id: number): Promise<Loan | null> {
    const { rows } = await this.db.query<Loan>(`${SELECT_LOAN} WHERE l.id = $1`, [id]);
    return rows[0] ?? null;
  }

  /** A member’s loans, newest first, keyset-paginated by id. */
  async forMember(memberId: number, openOnly: boolean, beforeId: number | null, limit: number): Promise<Loan[]> {
    const { rows } = await this.db.query<Loan>(
      `${SELECT_LOAN}
       WHERE l.member_id = $1
         AND (NOT $2::boolean OR l.returned_at IS NULL)
         AND ($3::bigint IS NULL OR l.id < $3)
       ORDER BY l.id DESC
       LIMIT $4`,
      [memberId, openOnly, beforeId, limit],
    );
    return rows;
  }
}
