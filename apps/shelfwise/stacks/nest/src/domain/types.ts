export interface Book {
  id: number;
  isbn: string;
  title: string;
  author: string;
  year: number | null;
  version: number;
}

export interface Copy {
  id: number;
  bookId: number;
  barcode: string;
  status: 'available' | 'lost' | 'retired';
}

export interface Member {
  id: number;
  email: string;
  name: string;
}

export interface Loan {
  id: number;
  copyId: number;
  memberId: number;
  bookId: number;
  title: string;
  barcode: string;
  loanedAt: Date;
  dueAt: Date;
  returnedAt: Date | null;
}

export interface Page<T> {
  items: T[];
  nextCursor: string | null;
  limit: number;
}
