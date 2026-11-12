import { describe, expect, it } from 'vitest';
import { createEvent, expectProblem, get, post } from './http';

const valid = () => ({
  name: 'Polyglot Live',
  venue: 'Arena',
  startsAt: '2027-06-01T20:00:00Z',
  sections: [
    { name: 'Floor', rows: 2, seatsPerRow: 3, priceCents: 5000 },
    { name: 'Balcony', rows: 1, seatsPerRow: 4, priceCents: 2500 },
  ],
});

describe('POST /events', () => {
  it('creates the event and its seats', async () => {
    const res = await post('/events', valid(), { as: 'olga' });
    expect(res.status).toBe(201);
    expect(res.headers.get('location')).toBe(`/events/${res.body.id}`);
    expect(res.body).toMatchObject({ name: 'Polyglot Live', venue: 'Arena', seatCount: 10 });
    expect(Date.parse(res.body.startsAt)).toBe(Date.parse('2027-06-01T20:00:00Z'));
  });

  it('lists every invalid field in a 422', async () => {
    const res = await post('/events', { name: '', venue: 'Arena', startsAt: 'tomorrow', sections: [] }, { as: 'olga' });
    expectProblem(res, 422, 'validation');
    expect(Object.keys(res.body.errors)).toEqual(expect.arrayContaining(['name', 'startsAt']));
    expect(Object.keys(res.body.errors).some((k) => k.startsWith('sections'))).toBe(true);
  });

  it('rejects invalid sections', async () => {
    const body = valid();
    body.sections[1]!.rows = 27;
    body.sections[1]!.name = 'Floor'; // duplicate name
    const res = await post('/events', body, { as: 'olga' });
    expectProblem(res, 422, 'validation');
    expect(Object.keys(res.body.errors).some((k) => k.startsWith('sections'))).toBe(true);
  });

  it('rejects unknown fields', async () => {
    const res = await post('/events', { ...valid(), soldOut: true }, { as: 'olga' });
    expectProblem(res, 422, 'validation');
    expect(res.body.errors).toHaveProperty('soldOut');
  });

  it('rejects malformed JSON with 400 invalid-body', async () => {
    expectProblem(await post('/events', '{"name": ', { as: 'olga' }), 400, 'invalid-body');
  });
});

describe('GET /events/{id}', () => {
  it('is public and cacheable', async () => {
    const { event } = await createEvent();
    const res = await get(`/events/${event.id}`);
    expect(res.status).toBe(200);
    expect(res.body).toMatchObject({ id: event.id, seatCount: 10 });
    expect(res.headers.get('cache-control')).toMatch(/public/);
    expect(res.headers.get('cache-control')).toMatch(/max-age=60/);
  });

  it('answers 400 invalid-id for a non-UUID and 404 for an unknown event', async () => {
    expectProblem(await get('/events/42'), 400, 'invalid-id');
    expectProblem(await get(`/events/${crypto.randomUUID()}`), 404, 'not-found');
  });
});

describe('GET /events/{id}/seats', () => {
  it('lists seats in section, row, number order with fresh availability', async () => {
    const { event } = await createEvent([
      { name: 'Floor', rows: 2, seatsPerRow: 2, priceCents: 5000 },
      { name: 'Balcony', rows: 1, seatsPerRow: 1, priceCents: 2500 },
    ]);
    const res = await get(`/events/${event.id}/seats`);
    expect(res.status).toBe(200);
    expect(res.headers.get('cache-control')).toMatch(/no-store/);
    expect(res.body.eventId).toBe(event.id);
    const labels = res.body.items.map((s: any) => `${s.section}-${s.row}${s.number}`);
    expect(labels).toEqual(['Balcony-A1', 'Floor-A1', 'Floor-A2', 'Floor-B1', 'Floor-B2']);
    expect(res.body.items.every((s: any) => s.status === 'available')).toBe(true);
    expect(res.body.items[0].priceCents).toBe(2500);
  });

  it('answers 404 for an unknown event', async () => {
    expectProblem(await get(`/events/${crypto.randomUUID()}/seats`), 404, 'not-found');
  });
});
