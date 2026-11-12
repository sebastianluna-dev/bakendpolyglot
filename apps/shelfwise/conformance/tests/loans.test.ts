import { describe, expect, it } from 'vitest';
import { createBook, createCopy, createMember, expectProblem, get, post } from './http';

const DAY = 24 * 60 * 60 * 1000;

async function setup() {
  const book = await createBook();
  const copy = await createCopy(book.id);
  const member = await createMember();
  return { book, copy, member };
}

describe('POST /loans', () => {
  it('lends a copy for LOAN_DAYS (default 21)', async () => {
    const { book, copy, member } = await setup();
    const res = await post('/loans', { copyId: copy.id, memberId: member.id });
    expect(res.status).toBe(201);
    expect(res.headers.get('location')).toBe(`/loans/${res.body.id}`);
    expect(res.body).toMatchObject({ copyId: copy.id, memberId: member.id, bookId: book.id, title: book.title, barcode: copy.barcode, returnedAt: null });
    const days = (Date.parse(res.body.dueAt) - Date.parse(res.body.loanedAt)) / DAY;
    expect(Math.round(days)).toBe(21);
  });

  it('refuses to lend a copy that is already on loan (409 copy-on-loan)', async () => {
    const { copy, member } = await setup();
    const other = await createMember();
    await post('/loans', { copyId: copy.id, memberId: member.id });
    expectProblem(await post('/loans', { copyId: copy.id, memberId: other.id }), 409, 'copy-on-loan');
  });

  it('lets exactly one of many concurrent loans of the same copy succeed', async () => {
    const { copy } = await setup();
    const members = await Promise.all(Array.from({ length: 8 }, () => createMember()));
    const results = await Promise.all(members.map((m) => post('/loans', { copyId: copy.id, memberId: m.id })));
    const statuses = results.map((r) => r.status).sort();
    expect(statuses.filter((s) => s === 201)).toHaveLength(1);
    expect(statuses.filter((s) => s === 409)).toHaveLength(7);
  });

  it('enforces MAX_LOANS_PER_MEMBER (default 5) with 409 loan-limit', async () => {
    const member = await createMember();
    for (let i = 0; i < 5; i++) {
      const book = await createBook();
      const copy = await createCopy(book.id);
      expect((await post('/loans', { copyId: copy.id, memberId: member.id })).status).toBe(201);
    }
    const book = await createBook();
    const copy = await createCopy(book.id);
    expectProblem(await post('/loans', { copyId: copy.id, memberId: member.id }), 409, 'loan-limit');
  });

  it('answers 404 for an unknown copy or member, 422 for a bad body', async () => {
    const { copy, member } = await setup();
    expectProblem(await post('/loans', { copyId: 999999999, memberId: member.id }), 404, 'not-found');
    expectProblem(await post('/loans', { copyId: copy.id, memberId: 999999999 }), 404, 'not-found');
    expectProblem(await post('/loans', { copyId: 'x' }), 422, 'validation');
  });
});

describe('POST /loans/{id}/return', () => {
  it('closes the loan, frees the copy, and refuses a second return', async () => {
    const { copy, member } = await setup();
    const loan = (await post('/loans', { copyId: copy.id, memberId: member.id })).body;

    const returned = await post(`/loans/${loan.id}/return`);
    expect(returned.status).toBe(200);
    expect(returned.body.id).toBe(loan.id);
    expect(typeof returned.body.returnedAt).toBe('string');

    expectProblem(await post(`/loans/${loan.id}/return`), 409, 'already-returned');
    expect((await post('/loans', { copyId: copy.id, memberId: member.id })).status).toBe(201);
  });

  it('answers 404 for an unknown loan', async () => {
    expectProblem(await post('/loans/999999999/return'), 404, 'not-found');
  });
});

describe('GET /members/{id}/loans', () => {
  it('lists newest first, filters open loans, and pages', async () => {
    const member = await createMember();
    const ids: number[] = [];
    for (let i = 0; i < 3; i++) {
      const book = await createBook();
      const copy = await createCopy(book.id);
      ids.push((await post('/loans', { copyId: copy.id, memberId: member.id })).body.id);
    }
    await post(`/loans/${ids[0]}/return`);

    const all = await get(`/members/${member.id}/loans`);
    expect(all.status).toBe(200);
    expect(all.body.items.map((l: any) => l.id)).toEqual([...ids].reverse());

    const open = await get(`/members/${member.id}/loans?status=open`);
    expect(open.body.items.map((l: any) => l.id)).toEqual([ids[2], ids[1]]);

    const page1 = await get(`/members/${member.id}/loans?limit=2`);
    expect(page1.body.items).toHaveLength(2);
    const page2 = await get(`/members/${member.id}/loans?limit=2&after=${encodeURIComponent(page1.body.nextCursor)}`);
    expect(page2.body.items.map((l: any) => l.id)).toEqual([ids[0]]);
    expect(page2.body.nextCursor).toBeNull();
  });

  it('answers 404 for an unknown member', async () => {
    expectProblem(await get('/members/999999999/loans'), 404, 'not-found');
  });
});
