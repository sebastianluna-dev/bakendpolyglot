// Fast tests: no database, no identity provider.
import { createHmac } from 'node:crypto';
import { describe, expect, it } from 'vitest';
import { loadConfig } from '../src/config.js';
import { visibleStatus } from '../src/holds/holds.service.js';
import { signatureMatches } from '../src/payments/webhooks.controller.js';

const env = {
  DATABASE_URL: 'postgres://x', OIDC_ISSUER: 'http://idp/realms/seatlock/', PAYMENTS_URL: 'http://pay', PAYMENT_WEBHOOK_SECRET: 's',
};

describe('config', () => {
  it('derives the JWKS URL from the issuer and applies defaults', () => {
    const c = loadConfig(env);
    expect(c.OIDC_JWKS_URL).toBe('http://idp/realms/seatlock/protocol/openid-connect/certs');
    expect(c.HOLD_TTL_SECONDS).toBe(600);
    expect(c.REDIS_URL).toBeUndefined();
  });

  it('reports every invalid setting at once', () => {
    expect(() => loadConfig({ HOLD_TTL_SECONDS: '0' })).toThrow(/DATABASE_URL[\s\S]*HOLD_TTL_SECONDS/);
  });
});

describe('hold status', () => {
  const now = new Date('2026-03-01T10:00:00Z');
  it('reads an active hold past its expiry as expired', () => {
    expect(visibleStatus('active', new Date('2026-03-01T09:59:59Z'), now)).toBe('expired');
    expect(visibleStatus('active', new Date('2026-03-01T10:05:00Z'), now)).toBe('active');
    expect(visibleStatus('checked_out', new Date('2026-03-01T09:00:00Z'), now)).toBe('checked_out');
  });
});

describe('webhook signatures', () => {
  const raw = Buffer.from('{"id":"evt_1"}');
  const good = createHmac('sha256', 'secret').update(raw).digest('hex');
  it('accepts the HMAC of the raw body and nothing else', () => {
    expect(signatureMatches(raw, good, 'secret')).toBe(true);
    expect(signatureMatches(raw, good, 'other')).toBe(false);
    expect(signatureMatches(Buffer.from('{"id": "evt_1"}'), good, 'secret')).toBe(false); // re-serialised
    expect(signatureMatches(raw, '', 'secret')).toBe(false);
  });
});
