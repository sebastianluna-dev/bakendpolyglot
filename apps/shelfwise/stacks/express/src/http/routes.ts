// The handler layer: bind, validate, call a repository or service, map to HTTP (Chapter 18).
import { Router } from 'express';
import type { BookRepository } from '../db/books.js';
import type { CopyRepository, MemberRepository } from '../db/copies-members.js';
import type { LoanRepository } from '../db/loans.js';
import { decodeCursor, encodeCursor } from '../domain/cursor.js';
import { notFound } from '../domain/errors.js';
import type { LoanService } from '../domain/loans.js';
import type { Book } from '../domain/types.js';
import {
  BookListQuery, CreateBookRequest, CreateCopyRequest, CreateLoanRequest, CreateMemberRequest,
  MemberLoansQuery, UpdateBookRequest, parse, parseId,
} from './validation.js';

export interface Deps {
  books: BookRepository;
  copies: CopyRepository;
  members: MemberRepository;
  loans: LoanRepository;
  loanService: LoanService;
}

const etagOf = (b: Book) => `"book-${b.id}-v${b.version}"`;

/** Parses If-Match: "book-{id}-v{n}" → n. A header that names another book or version never matches. */
function expectedVersion(header: string | undefined, id: number): number | null {
  if (!header) return null;
  const m = /^"book-(\d+)-v(\d+)"$/.exec(header.trim());
  return m && Number(m[1]) === id ? Number(m[2]) : -1;
}

type BookCursor = { title: string; id: number };
const isBookCursor = (v: unknown): v is BookCursor =>
  typeof v === 'object' && v !== null && typeof (v as BookCursor).title === 'string' && Number.isInteger((v as BookCursor).id);
const isIdCursor = (v: unknown): v is { id: number } =>
  typeof v === 'object' && v !== null && Number.isInteger((v as { id: number }).id);

export function routes(d: Deps): Router {
  const r = Router();

  r.get('/health', (_req, res) => {
    res.json({ status: 'ok' });
  });

  r.get('/books', async (req, res) => {
    const q = parse(BookListQuery, req.query);
    const after = q.after ? decodeCursor(q.after, isBookCursor) : null;
    const rows = await d.books.search(q.q ?? null, after, q.limit + 1); // one extra row tells us if there is a next page
    const items = rows.slice(0, q.limit);
    const last = items.at(-1);
    res.json({
      items,
      nextCursor: rows.length > q.limit && last ? encodeCursor({ title: last.title, id: last.id }) : null,
      limit: q.limit,
    });
  });

  r.post('/books', async (req, res) => {
    const book = await d.books.create(parse(CreateBookRequest, req.body));
    res.status(201).location(`/books/${book.id}`).set('ETag', etagOf(book)).json(book);
  });

  r.get('/books/:id', async (req, res) => {
    const id = parseId(req.params.id);
    const book = await d.books.find(id);
    if (!book) throw notFound('Book', id);
    res.set('ETag', etagOf(book));
    if (req.get('if-none-match') === etagOf(book)) {
      res.status(304).end();
      return;
    }
    res.json(book);
  });

  r.patch('/books/:id', async (req, res) => {
    const id = parseId(req.params.id);
    const changes = parse(UpdateBookRequest, req.body);
    const book = await d.books.update(id, changes, expectedVersion(req.get('if-match'), id));
    res.set('ETag', etagOf(book)).json(book);
  });

  r.post('/books/:id/copies', async (req, res) => {
    const bookId = parseId(req.params.id);
    const { barcode } = parse(CreateCopyRequest, req.body);
    const copy = await d.copies.create(bookId, barcode);
    res.status(201).location(`/copies/${copy.id}`).json(copy);
  });

  r.post('/members', async (req, res) => {
    const { email, name } = parse(CreateMemberRequest, req.body);
    const member = await d.members.create(email, name);
    res.status(201).location(`/members/${member.id}`).json(member);
  });

  r.get('/members/:id/loans', async (req, res) => {
    const memberId = parseId(req.params.id);
    const q = parse(MemberLoansQuery, req.query);
    if (!(await d.members.exists(memberId))) throw notFound('Member', memberId);
    const before = q.after ? decodeCursor(q.after, isIdCursor).id : null;
    const rows = await d.loans.forMember(memberId, q.status === 'open', before, q.limit + 1);
    const items = rows.slice(0, q.limit);
    const last = items.at(-1);
    res.json({ items, nextCursor: rows.length > q.limit && last ? encodeCursor({ id: last.id }) : null, limit: q.limit });
  });

  r.post('/loans', async (req, res) => {
    const { copyId, memberId } = parse(CreateLoanRequest, req.body);
    const loan = await d.loanService.lend(copyId, memberId);
    res.status(201).location(`/loans/${loan.id}`).json(loan);
  });

  r.post('/loans/:id/return', async (req, res) => {
    res.json(await d.loanService.returnLoan(parseId(req.params.id)));
  });

  return r;
}
