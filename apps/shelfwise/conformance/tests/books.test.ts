import { describe, expect, it } from 'vitest';
import { createBook, expectProblem, get, isbn, patch, post, uid } from './http';

describe('health', () => {
  it('answers 200 {status: ok}', async () => {
    const res = await get('/health');
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ status: 'ok' });
  });
});

describe('POST /books', () => {
  it('creates a book: 201, Location, ETag and the book', async () => {
    const body = { title: 'The Left Hand of Darkness', author: 'Ursula K. Le Guin', isbn: isbn(), year: 1969 };
    const res = await post('/books', body);
    expect(res.status).toBe(201);
    expect(res.body).toMatchObject({ ...body, version: 1 });
    expect(typeof res.body.id).toBe('number');
    expect(res.headers.get('location')).toBe(`/books/${res.body.id}`);
    expect(res.headers.get('etag')).toBe(`"book-${res.body.id}-v1"`);
  });

  it('accepts a book without a year (null)', async () => {
    const book = await createBook({ year: undefined });
    expect(book.year).toBeNull();
  });

  it('rejects a duplicate ISBN with 409 duplicate-isbn', async () => {
    const book = await createBook();
    expectProblem(await post('/books', { title: 'Copy', author: 'Someone', isbn: book.isbn }), 409, 'duplicate-isbn');
  });

  it('rejects invalid fields with 422 validation and lists each field', async () => {
    const res = await post('/books', { title: '', author: 'A', isbn: '123' });
    expectProblem(res, 422, 'validation');
    expect(Object.keys(res.body.errors).sort()).toEqual(['isbn', 'title']);
  });

  it('rejects a year in the future', async () => {
    const res = await post('/books', { title: 'Later', author: 'A', isbn: isbn(), year: new Date().getFullYear() + 1 });
    expectProblem(res, 422, 'validation');
    expect(res.body.errors).toHaveProperty('year');
  });

  it('rejects unknown fields (no mass assignment)', async () => {
    const res = await post('/books', { title: 'T', author: 'A', isbn: isbn(), version: 99 });
    expectProblem(res, 422, 'validation');
    expect(res.body.errors).toHaveProperty('version');
  });

  it('rejects malformed JSON with 400 invalid-body', async () => {
    expectProblem(await post('/books', '{"title": '), 400, 'invalid-body');
  });
});

describe('GET /books/{id}', () => {
  it('returns the book with a version ETag', async () => {
    const book = await createBook();
    const res = await get(`/books/${book.id}`);
    expect(res.status).toBe(200);
    expect(res.body).toEqual(book);
    expect(res.headers.get('etag')).toBe(`"book-${book.id}-v1"`);
  });

  it('answers 304 when If-None-Match matches', async () => {
    const book = await createBook();
    const res = await get(`/books/${book.id}`, { 'If-None-Match': `"book-${book.id}-v1"` });
    expect(res.status).toBe(304);
  });

  it('answers 404 not-found for an unknown id', async () => {
    expectProblem(await get('/books/999999999'), 404, 'not-found');
  });

  it('answers 400 invalid-id for a non-numeric id', async () => {
    expectProblem(await get('/books/abc'), 400, 'invalid-id');
  });
});

describe('PATCH /books/{id}', () => {
  it('updates fields and increments the version', async () => {
    const book = await createBook();
    const res = await patch(`/books/${book.id}`, { title: 'Dune Messiah', year: 1969 }, { 'If-Match': `"book-${book.id}-v1"` });
    expect(res.status).toBe(200);
    expect(res.body).toMatchObject({ id: book.id, title: 'Dune Messiah', year: 1969, author: book.author, version: 2 });
    expect(res.headers.get('etag')).toBe(`"book-${book.id}-v2"`);
  });

  it('answers 412 version-mismatch for a stale If-Match', async () => {
    const book = await createBook();
    await patch(`/books/${book.id}`, { title: 'First edit' });
    expectProblem(await patch(`/books/${book.id}`, { title: 'Stale edit' }, { 'If-Match': `"book-${book.id}-v1"` }), 412, 'version-mismatch');
  });

  it('rejects an empty or invalid body with 422', async () => {
    const book = await createBook();
    expectProblem(await patch(`/books/${book.id}`, {}), 422, 'validation');
    expectProblem(await patch(`/books/${book.id}`, { isbn: isbn() }), 422, 'validation');
  });

  it('answers 404 for an unknown book', async () => {
    expectProblem(await patch('/books/999999999', { title: 'X' }), 404, 'not-found');
  });
});

describe('GET /books (search and cursor pagination)', () => {
  it('filters by q and pages with an opaque cursor, sorted by title', async () => {
    const tag = `Zq${uid()}`;
    for (const suffix of ['c', 'a', 'b']) await createBook({ title: `${tag} ${suffix}` });

    const first = await get(`/books?q=${tag}&limit=2`);
    expect(first.status).toBe(200);
    expect(first.body.limit).toBe(2);
    expect(first.body.items.map((b: any) => b.title)).toEqual([`${tag} a`, `${tag} b`]);
    expect(typeof first.body.nextCursor).toBe('string');

    const second = await get(`/books?q=${tag}&limit=2&after=${encodeURIComponent(first.body.nextCursor)}`);
    expect(second.body.items.map((b: any) => b.title)).toEqual([`${tag} c`]);
    expect(second.body.nextCursor).toBeNull();
  });

  it('matches the author too, case-insensitively', async () => {
    const author = `Author${uid()}`;
    await createBook({ author });
    const res = await get(`/books?q=${author.toLowerCase()}`);
    expect(res.body.items).toHaveLength(1);
  });

  it('rejects a malformed cursor with 400 bad-cursor', async () => {
    expectProblem(await get('/books?after=not-a-cursor!!'), 400, 'bad-cursor');
  });

  it('rejects an out-of-range limit with 422', async () => {
    expectProblem(await get('/books?limit=0'), 422, 'validation');
    expectProblem(await get('/books?limit=101'), 422, 'validation');
  });
});
