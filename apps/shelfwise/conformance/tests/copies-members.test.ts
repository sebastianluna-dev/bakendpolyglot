import { describe, expect, it } from 'vitest';
import { createBook, createMember, expectProblem, post, uid } from './http';

describe('POST /books/{id}/copies', () => {
  it('adds a copy', async () => {
    const book = await createBook();
    const barcode = `C-${uid()}`;
    const res = await post(`/books/${book.id}/copies`, { barcode });
    expect(res.status).toBe(201);
    expect(res.body).toMatchObject({ bookId: book.id, barcode, status: 'available' });
  });

  it('rejects a duplicate barcode with 409', async () => {
    const book = await createBook();
    const barcode = `C-${uid()}`;
    await post(`/books/${book.id}/copies`, { barcode });
    expectProblem(await post(`/books/${book.id}/copies`, { barcode }), 409, 'duplicate-barcode');
  });

  it('answers 404 for an unknown book and 422 for a missing barcode', async () => {
    expectProblem(await post('/books/999999999/copies', { barcode: `C-${uid()}` }), 404, 'not-found');
    const book = await createBook();
    expectProblem(await post(`/books/${book.id}/copies`, {}), 422, 'validation');
  });
});

describe('POST /members', () => {
  it('registers a member with a Location header', async () => {
    const email = `m-${uid()}@example.com`;
    const res = await post('/members', { email, name: 'Ben' });
    expect(res.status).toBe(201);
    expect(res.body).toMatchObject({ email, name: 'Ben' });
    expect(res.headers.get('location')).toBe(`/members/${res.body.id}`);
  });

  it('rejects a duplicate email with 409 and an invalid one with 422', async () => {
    const member = await createMember();
    expectProblem(await post('/members', { email: member.email, name: 'Other' }), 409, 'duplicate-email');
    expectProblem(await post('/members', { email: 'not-an-email', name: 'X' }), 422, 'validation');
  });
});
