import { describe, expect, it } from 'vitest';
import { checkout, createEvent, expectProblem, get, hold, paymentEvent, seatStatuses, webhook } from './http';

async function pendingOrder() {
  const { event, seats } = await createEvent();
  const h = await hold('alice', event.id, [seats[0]!.id, seats[1]!.id]);
  const order = await checkout('alice', h.body.id);
  expect(order.status, JSON.stringify(order.body)).toBe(201);
  return { event, hold: h.body, order: order.body };
}

describe('POST /webhooks/payments', () => {
  it('rejects a bad or missing signature with 401', async () => {
    const { order } = await pendingOrder();
    const event = paymentEvent('payment_intent.succeeded', order.paymentIntentId);
    expectProblem(await webhook(event, 'deadbeef'), 401, 'invalid-signature');
    expectProblem(await webhook(event, ''), 401, 'invalid-signature');
    expect((await get(`/orders/${order.id}`, { as: 'alice' })).body.status).toBe('pending');
  });

  it('marks the order paid and the seats sold', async () => {
    const { event, hold: h, order } = await pendingOrder();
    expect((await webhook(paymentEvent('payment_intent.succeeded', order.paymentIntentId))).status).toBe(204);
    expect((await get(`/orders/${order.id}`, { as: 'alice' })).body.status).toBe('paid');
    const statuses = await seatStatuses(event.id);
    for (const id of h.seatIds) expect(statuses[id]).toBe('sold');
  });

  it('accepts duplicates and ignores late events that would undo a final state', async () => {
    const { order } = await pendingOrder();
    const succeeded = paymentEvent('payment_intent.succeeded', order.paymentIntentId);
    expect((await webhook(succeeded)).status).toBe(204);
    expect((await webhook(succeeded)).status).toBe(204); // same event id
    expect((await webhook(paymentEvent('payment_intent.failed', order.paymentIntentId))).status).toBe(204);
    expect((await get(`/orders/${order.id}`, { as: 'alice' })).body.status).toBe('paid');
  });

  it('marks the order failed and releases the seats on failure', async () => {
    const { event, hold: h, order } = await pendingOrder();
    expect((await webhook(paymentEvent('payment_intent.failed', order.paymentIntentId))).status).toBe(204);
    expect((await get(`/orders/${order.id}`, { as: 'alice' })).body.status).toBe('failed');
    expect((await get(`/holds/${h.id}`, { as: 'alice' })).body.status).toBe('released');
    const statuses = await seatStatuses(event.id);
    for (const id of h.seatIds) expect(statuses[id]).toBe('available');
    expect((await hold('bob', event.id, h.seatIds)).status).toBe(201);
  });

  it('acknowledges events for unknown payment intents', async () => {
    expect((await webhook(paymentEvent('payment_intent.succeeded', 'pi_unknown'))).status).toBe(204);
  });

  it('rejects a signed body that is not a payment event', async () => {
    expectProblem(await webhook({ hello: 'world' }), 400, 'invalid-body');
  });
});
