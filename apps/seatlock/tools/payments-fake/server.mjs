// A fake payment provider (Chapter 24). Stacks create payment intents here with an Idempotency-Key;
// with AUTO_WEBHOOKS=1 it later posts signed webhooks to WEBHOOK_URL — sometimes twice and sometimes
// late, on purpose. Amounts ending in 13 cents fail with 503, so tests can exercise provider outages.
import { createServer } from 'node:http';
import { createHmac, randomUUID } from 'node:crypto';

const PORT = Number(process.env.PORT ?? 4000);
const SECRET = process.env.PAYMENT_WEBHOOK_SECRET ?? 'whsec_dev';
const WEBHOOK_URL = process.env.WEBHOOK_URL ?? 'http://localhost:3000/webhooks/payments';
const AUTO = process.env.AUTO_WEBHOOKS === '1';
const FAIL_RATE = Number(process.env.FAIL_RATE ?? 0.1);
const DUPLICATE_RATE = Number(process.env.DUPLICATE_RATE ?? 0.2);

const byKey = new Map(); // Idempotency-Key → intent

function send(res, status, body) {
  res.writeHead(status, { 'content-type': 'application/json' });
  res.end(JSON.stringify(body));
}

async function deliver(event, attempt = 1) {
  const body = JSON.stringify(event);
  const signature = createHmac('sha256', SECRET).update(body).digest('hex');
  try {
    const r = await fetch(WEBHOOK_URL, { method: 'POST', headers: { 'content-type': 'application/json', 'x-signature': signature }, body });
    if (r.status >= 500 && attempt < 5) setTimeout(() => deliver(event, attempt + 1), 200 * attempt); // providers retry
  } catch {
    if (attempt < 5) setTimeout(() => deliver(event, attempt + 1), 200 * attempt);
  }
}

function schedule(intent) {
  const type = Math.random() < FAIL_RATE ? 'payment_intent.failed' : 'payment_intent.succeeded';
  const event = { id: `evt_${randomUUID()}`, type, data: { paymentIntentId: intent.id, orderId: intent.orderId } };
  setTimeout(() => deliver(event), 50 + Math.random() * 450);
  if (Math.random() < DUPLICATE_RATE) setTimeout(() => deliver(event), 200 + Math.random() * 1500); // same id again
}

createServer((req, res) => {
  if (req.method === 'GET' && req.url === '/health') return send(res, 200, { status: 'ok' });
  if (req.method === 'POST' && req.url === '/payment-intents') {
    let raw = '';
    req.on('data', (c) => (raw += c));
    req.on('end', () => {
      const key = req.headers['idempotency-key'];
      if (!key) return send(res, 400, { error: 'Idempotency-Key header is required' });
      let body;
      try {
        body = JSON.parse(raw);
      } catch {
        return send(res, 400, { error: 'invalid JSON' });
      }
      if (!Number.isInteger(body.amountCents) || typeof body.orderId !== 'string') return send(res, 400, { error: 'amountCents and orderId are required' });
      if (body.amountCents % 100 === 13) return send(res, 503, { error: 'provider unavailable (simulated)' });
      const existing = byKey.get(key);
      if (existing) return send(res, 200, existing);
      const intent = { id: `pi_${randomUUID().replaceAll('-', '')}`, orderId: body.orderId, amountCents: body.amountCents, currency: body.currency ?? 'USD', status: 'requires_payment' };
      byKey.set(key, intent);
      if (AUTO) schedule(intent);
      return send(res, 201, intent);
    });
    return;
  }
  send(res, 404, { error: 'not_found' });
}).listen(PORT, () => console.log(`fake payments on :${PORT} (auto webhooks: ${AUTO})`));
