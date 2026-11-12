import { describe, expect, it } from 'vitest';
import { createEvent, expectProblem, get, post, token } from './http';

// Chapters 19–21: every protected endpoint validates the token, then checks roles.
describe('authentication', () => {
  it('serves /health without a token', async () => {
    expect((await get('/health')).status).toBe(200);
  });

  it('rejects a request without a token with 401 and a Bearer challenge', async () => {
    const res = await post('/holds', { eventId: crypto.randomUUID(), seatIds: [crypto.randomUUID()] });
    expectProblem(res, 401, 'unauthorized');
    expect(res.headers.get('www-authenticate') ?? '').toMatch(/^Bearer/i);
  });

  it('rejects a malformed token', async () => {
    expectProblem(await post('/holds', {}, { bearer: 'not-a-jwt' }), 401, 'unauthorized');
  });

  it('rejects a token whose payload was altered (signature no longer matches)', async () => {
    const [h, p, s] = (await token('alice')).split('.');
    const claims = JSON.parse(Buffer.from(p!, 'base64url').toString());
    claims.realm_access = { roles: ['customer', 'box-office'] };
    const forged = `${h}.${Buffer.from(JSON.stringify(claims)).toString('base64url')}.${s}`;
    expectProblem(await post('/events', {}, { bearer: forged }), 401, 'unauthorized');
  });

  it('rejects a valid token issued for another audience', async () => {
    expectProblem(await post('/holds', {}, { bearer: await token('alice', 'other-app') }), 401, 'unauthorized');
  });
});

describe('authorization by role', () => {
  it('lets only box-office create events (403 for a customer)', async () => {
    const body = { name: 'X', venue: 'Y', startsAt: '2027-01-01T20:00:00Z', sections: [{ name: 'A', rows: 1, seatsPerRow: 1, priceCents: 100 }] };
    expectProblem(await post('/events', body, { as: 'alice' }), 403, 'forbidden');
  });

  it('lets only box-office run the reaper (403 for a customer)', async () => {
    expectProblem(await post('/admin/holds/release-expired', undefined, { as: 'alice' }), 403, 'forbidden');
  });

  it('lets only customers hold seats (403 for box-office)', async () => {
    const { event, seats } = await createEvent();
    expectProblem(await post('/holds', { eventId: event.id, seatIds: [seats[0]!.id] }, { as: 'olga' }), 403, 'forbidden');
  });
});
