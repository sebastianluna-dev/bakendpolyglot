import { describe, expect, it } from 'vitest';
import { HOLD_TTL_SECONDS, createEvent, del, expectProblem, get, hold, post, seatStatuses } from './http';

describe('POST /holds', () => {
  it('holds seats atomically and reports them held', async () => {
    const { event, seats } = await createEvent();
    const ids = [seats[3]!.id, seats[1]!.id];
    const before = Date.now();
    const res = await hold('alice', event.id, ids);
    expect(res.status, JSON.stringify(res.body)).toBe(201);
    expect(res.headers.get('location')).toBe(`/holds/${res.body.id}`);
    expect(res.body).toMatchObject({ eventId: event.id, status: 'active', seatIds: [...ids].sort() });
    const expires = Date.parse(res.body.expiresAt);
    expect(expires).toBeGreaterThan(before + (HOLD_TTL_SECONDS - 2) * 1000);
    expect(expires).toBeLessThan(Date.now() + (HOLD_TTL_SECONDS + 2) * 1000);
    const statuses = await seatStatuses(event.id);
    expect(statuses[ids[0]!]).toBe('held');
    expect(statuses[seats[0]!.id]).toBe('available');
  });

  it('is all or nothing: one taken seat fails the whole hold with 409 and names it', async () => {
    const { event, seats } = await createEvent();
    expect((await hold('alice', event.id, [seats[0]!.id])).status).toBe(201);
    const res = await hold('bob', event.id, [seats[0]!.id, seats[1]!.id, seats[2]!.id]);
    expectProblem(res, 409, 'seats-unavailable');
    expect(res.body.seatIds).toEqual([seats[0]!.id]);
    const statuses = await seatStatuses(event.id);
    expect(statuses[seats[1]!.id]).toBe('available'); // rolled back
    expect(statuses[seats[2]!.id]).toBe('available');
  });

  it('treats a seat of another event as unavailable', async () => {
    const a = await createEvent();
    const b = await createEvent();
    const res = await hold('alice', a.event.id, [a.seats[0]!.id, b.seats[0]!.id]);
    expectProblem(res, 409, 'seats-unavailable');
    expect(res.body.seatIds).toEqual([b.seats[0]!.id]);
  });

  it('validates the request', async () => {
    const { event, seats } = await createEvent();
    const seven = seats.slice(0, 7).map((s) => s.id);
    expectProblem(await hold('alice', event.id, seven), 422, 'validation');
    expectProblem(await hold('alice', event.id, []), 422, 'validation');
    expectProblem(await hold('alice', event.id, [seats[0]!.id, seats[0]!.id]), 422, 'validation');
    const res = await post('/holds', { eventId: 'nope', seatIds: ['x'] }, { as: 'alice' });
    expectProblem(res, 422, 'validation');
    expect(Object.keys(res.body.errors)).toContain('eventId');
  });

  it('answers 404 for an unknown event', async () => {
    expectProblem(await hold('alice', crypto.randomUUID(), [crypto.randomUUID()]), 404, 'not-found');
  });

  it('lets exactly one of many concurrent buyers win the same seats', async () => {
    const { event, seats } = await createEvent();
    const ids = [seats[0]!.id, seats[1]!.id];
    const results = await Promise.all(Array.from({ length: 20 }, (_, i) => hold(i % 2 ? 'bob' : 'alice', event.id, ids)));
    const statuses = results.map((r) => r.status).sort();
    expect(statuses.filter((s) => s === 201)).toHaveLength(1);
    expect(statuses.filter((s) => s === 409)).toHaveLength(19);
  });

  it('never splits overlapping concurrent holds', async () => {
    const { event, seats } = await createEvent();
    const [a, b, c] = seats.map((s) => s.id);
    const results = await Promise.all([hold('alice', event.id, [a!, b!]), hold('bob', event.id, [b!, c!])]);
    const won = results.filter((r) => r.status === 201);
    expect(won).toHaveLength(1);
    const statuses = await seatStatuses(event.id);
    const held = [a!, b!, c!].filter((id) => statuses[id] === 'held');
    expect(held.sort()).toEqual([...won[0]!.body.seatIds].sort());
  });
});

describe('GET and DELETE /holds/{id}', () => {
  it('shows a hold only to its holder (404 for anyone else)', async () => {
    const { event, seats } = await createEvent();
    const h = await hold('alice', event.id, [seats[0]!.id]);
    expect((await get(`/holds/${h.body.id}`, { as: 'alice' })).body).toMatchObject({ id: h.body.id, status: 'active' });
    expectProblem(await get(`/holds/${h.body.id}`, { as: 'bob' }), 404, 'not-found');
    expectProblem(await del(`/holds/${h.body.id}`, { as: 'bob' }), 404, 'not-found');
    expectProblem(await get(`/holds/${crypto.randomUUID()}`, { as: 'alice' }), 404, 'not-found');
    expectProblem(await get('/holds/123', { as: 'alice' }), 400, 'invalid-id');
  });

  it('releases a hold and its seats', async () => {
    const { event, seats } = await createEvent();
    const h = await hold('alice', event.id, [seats[0]!.id, seats[1]!.id]);
    expect((await del(`/holds/${h.body.id}`, { as: 'alice' })).status).toBe(204);
    expect((await get(`/holds/${h.body.id}`, { as: 'alice' })).body.status).toBe('released');
    const statuses = await seatStatuses(event.id);
    expect(statuses[seats[0]!.id]).toBe('available');
    expectProblem(await del(`/holds/${h.body.id}`, { as: 'alice' }), 409, 'hold-not-active');
    expect((await hold('bob', event.id, [seats[0]!.id])).status).toBe(201); // back on sale
  });
});
