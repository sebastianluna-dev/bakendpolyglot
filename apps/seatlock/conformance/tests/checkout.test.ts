import { randomUUID } from 'node:crypto';
import { describe, expect, it } from 'vitest';
import { checkout, createEvent, del, expectProblem, get, hold, post, seatStatuses } from './http';

async function heldSeats(n = 2, priceCents = 2500) {
  const { event, seats } = await createEvent([{ name: 'Floor', rows: 1, seatsPerRow: 6, priceCents }]);
  const h = await hold('alice', event.id, seats.slice(0, n).map((s) => s.id));
  expect(h.status, JSON.stringify(h.body)).toBe(201);
  return { event, seats, hold: h.body };
}

describe('POST /holds/{id}/checkout', () => {
  it('requires an Idempotency-Key', async () => {
    const { hold: h } = await heldSeats();
    expectProblem(await post(`/holds/${h.id}/checkout`, undefined, { as: 'alice' }), 400, 'idempotency-key-required');
  });

  it('creates a pending order with a payment intent', async () => {
    const { event, hold: h } = await heldSeats(2, 2500);
    const res = await checkout('alice', h.id);
    expect(res.status, JSON.stringify(res.body)).toBe(201);
    expect(res.headers.get('location')).toBe(`/orders/${res.body.id}`);
    expect(res.body).toMatchObject({ holdId: h.id, eventId: event.id, status: 'pending', totalCents: 5000, currency: 'USD' });
    expect(res.body.paymentIntentId).toMatch(/^pi_/);
    expect([...res.body.seatIds].sort()).toEqual(h.seatIds);
    expect((await get(`/orders/${res.body.id}`, { as: 'alice' })).body).toMatchObject({ id: res.body.id, status: 'pending' });
    expect((await get(`/holds/${h.id}`, { as: 'alice' })).body.status).toBe('checked_out');
    expect((await seatStatuses(event.id))[h.seatIds[0]]).toBe('held');
  });

  it('lets only the holder check out or read the order', async () => {
    const { hold: h } = await heldSeats();
    expectProblem(await checkout('bob', h.id), 404, 'not-found');
    const order = await checkout('alice', h.id);
    expectProblem(await get(`/orders/${order.body.id}`, { as: 'bob' }), 404, 'not-found');
  });

  it('replays the stored response for a retry with the same key', async () => {
    const { hold: h } = await heldSeats();
    const key = randomUUID();
    const first = await checkout('alice', h.id, key);
    const retry = await checkout('alice', h.id, key);
    expect(retry.status).toBe(201);
    expect(retry.body).toEqual(first.body);
    expect(retry.headers.get('idempotent-replayed')).toBe('true');
  });

  it('runs once when the same key arrives concurrently', async () => {
    const { hold: h } = await heldSeats();
    const key = randomUUID();
    const results = await Promise.all(Array.from({ length: 8 }, () => checkout('alice', h.id, key)));
    for (const r of results) expect([201, 409], JSON.stringify(r.body)).toContain(r.status);
    const ok = results.filter((r) => r.status === 201);
    expect(ok.length).toBeGreaterThanOrEqual(1);
    expect(new Set(ok.map((r) => r.body.id)).size).toBe(1);
    for (const r of results.filter((x) => x.status === 409)) expectProblem(r, 409, 'idempotency-in-progress');
  });

  it('rejects a key reused for a different hold', async () => {
    const a = await heldSeats();
    const b = await heldSeats();
    const key = randomUUID();
    expect((await checkout('alice', a.hold.id, key)).status).toBe(201);
    expectProblem(await checkout('alice', b.hold.id, key), 422, 'idempotency-key-reuse');
  });

  it('scopes keys to the user', async () => {
    const a = await heldSeats();
    const { event, seats } = await createEvent();
    const bobs = await hold('bob', event.id, [seats[0]!.id]);
    const key = randomUUID();
    expect((await checkout('alice', a.hold.id, key)).status).toBe(201);
    expect((await checkout('bob', bobs.body.id, key)).status).toBe(201);
  });

  it('does not check out the same hold twice with a new key', async () => {
    const { hold: h } = await heldSeats();
    expect((await checkout('alice', h.id)).status).toBe(201);
    expectProblem(await checkout('alice', h.id), 409, 'hold-not-active');
  });

  it('does not check out a released hold', async () => {
    const { hold: h } = await heldSeats();
    await del(`/holds/${h.id}`, { as: 'alice' });
    expectProblem(await checkout('alice', h.id), 409, 'hold-not-active');
  });

  it('answers 502 when the provider fails, without storing the response', async () => {
    const { hold: h } = await heldSeats(1, 1313); // the fake provider fails amounts ending in 13 cents
    const key = randomUUID();
    expectProblem(await checkout('alice', h.id, key), 502, 'payment-provider-unavailable');
    const retry = await checkout('alice', h.id, key);
    expectProblem(retry, 502, 'payment-provider-unavailable');
    expect(retry.headers.get('idempotent-replayed')).toBeNull();
  });
});
