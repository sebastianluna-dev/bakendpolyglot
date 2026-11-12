import { describe, expect, it } from 'vitest';
import { HOLD_TTL_SECONDS, checkout, createEvent, expectProblem, get, hold, post, reaperToken, seatStatuses, sleep } from './http';

// Runs last (alphabetically it doesn't, but it only needs its own holds to expire).
describe('hold expiry and the reaper', () => {
  it('expires holds, refuses their checkout, and the reaper returns the seats to sale', async () => {
    const { event, seats } = await createEvent();
    const h = await hold('alice', event.id, [seats[0]!.id, seats[1]!.id]);
    expect(h.status).toBe(201);
    await sleep((HOLD_TTL_SECONDS + 1) * 1000);

    expect((await get(`/holds/${h.body.id}`, { as: 'alice' })).body.status).toBe('expired');
    expectProblem(await checkout('alice', h.body.id), 410, 'hold-expired');

    const run = await post('/admin/holds/release-expired', undefined, { bearer: await reaperToken() });
    expect(run.status, JSON.stringify(run.body)).toBe(200);
    expect(typeof run.body.released).toBe('number');

    const statuses = await seatStatuses(event.id);
    expect(statuses[seats[0]!.id]).toBe('available');
    expect((await hold('bob', event.id, [seats[0]!.id])).status).toBe(201);

    const again = await post('/admin/holds/release-expired', undefined, { bearer: await reaperToken() });
    expect(again.status).toBe(200); // idempotent: nothing left to release for this hold
    expect((await get(`/holds/${h.body.id}`, { as: 'alice' })).body.status).toBe('expired');
  });
});
