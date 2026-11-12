// Domain errors. The HTTP layer maps each one to a Problem Details response (Chapter 12).
export class DomainError extends Error {
  constructor(
    readonly code: string,
    readonly status: number,
    readonly title: string,
    detail: string,
    readonly extensions: Record<string, unknown> = {},
  ) {
    super(detail);
  }
}

export const notFound = (what: string, id: number | string) =>
  new DomainError('not-found', 404, 'Resource not found', `${what} ${id} does not exist.`);

export const duplicateIsbn = (isbn: string) =>
  new DomainError('duplicate-isbn', 409, 'ISBN already in the catalog', `A book with ISBN ${isbn} already exists.`, { isbn });

export const duplicateBarcode = (barcode: string) =>
  new DomainError('duplicate-barcode', 409, 'Barcode already in use', `A copy with barcode ${barcode} already exists.`, { barcode });

export const duplicateEmail = (email: string) =>
  new DomainError('duplicate-email', 409, 'Email already registered', `A member with email ${email} already exists.`);

export const copyOnLoan = (copyId: number) =>
  new DomainError('copy-on-loan', 409, 'Copy is already on loan', `Copy ${copyId} has an open loan.`, { copyId });

export const loanLimit = (memberId: number, max: number) =>
  new DomainError('loan-limit', 409, 'Loan limit reached', `Member ${memberId} already has ${max} open loans.`, { memberId, max });

export const alreadyReturned = (loanId: number) =>
  new DomainError('already-returned', 409, 'Loan already returned', `Loan ${loanId} was already returned.`);

export const versionMismatch = (id: number) =>
  new DomainError('version-mismatch', 412, 'Book was changed by someone else', `Book ${id} does not match the If-Match version.`);
