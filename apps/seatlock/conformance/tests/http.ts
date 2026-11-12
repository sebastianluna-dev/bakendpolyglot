// Tiny HTTP helpers. The suite only speaks HTTP, so it runs against any stack.
import { createHmac, randomUUID } from 'node:crypto';
import { expect } from 'vitest';

export const BASE_URL = (process.env.SEATLOCK_URL ?? 'http://localhost:3000').replace(/\/$/, '');
const TOKEN_URL = process.env.TOKEN_URL ?? 'http://localhost:8081/realms/seatlock/protocol/openid-connect/token';
export const WEBHOOK_SECRET = process.env.PAYMENT_WEBHOOK_SECRET ?? 'whsec_dev';
export const HOLD_TTL_SECONDS = Number(process.env.HOLD_TTL_SECONDS ?? 4);

export interface Res<T = any> {
  status: number;
  headers: Headers;
  body: T;
}

export type User = 'alice' | 'bob' | 'olga';

const tokens = new Map<string, Promise<string>>();

async function fetchToken(form: Record<string, string>): Promise<string> {
  const r = await fetch(TOKEN_URL, { method: 'POST', body: new URLSearchParams(form) });
  if (!r.ok) throw new Error(`token request failed: ${r.status} ${await r.text()}`);
  return ((await r.json()) as { access_token: string }).access_token;
}

/** A password-grant token, as the seatlock-tests client (or another client, to test audiences). */
export function token(user: User, clientId = 'seatlock-tests'): Promise<string> {
  const key = `${clientId}:${user}`;
  if (!tokens.has(key)) tokens.set(key, fetchToken({ grant_type: 'password', client_id: clientId, username: user, password: 'password' }));
  return tokens.get(key)!;
}

/** The hold-reaper's client-credentials token (Chapter 20). */
export function reaperToken(): Promise<string> {
  if (!tokens.has('reaper')) tokens.set('reaper', fetchToken({ grant_type: 'client_credentials', client_id: 'hold-reaper', client_secret: 'reaper-secret' }));
  return tokens.get('reaper')!;
}

interface CallOptions {
  as?: User | null;
  bearer?: string;
  headers?: Record<string, string>;
}

export async function call<T = any>(method: string, path: string, body?: unknown, opts: CallOptions = {}): Promise<Res<T>> {
  const headers: Record<string, string> = { Accept: 'application/json', ...opts.headers };
  const bearer = opts.bearer ?? (opts.as ? await token(opts.as) : undefined);
  if (bearer) headers.Authorization = `Bearer ${bearer}`;
  const init: RequestInit = { method, headers };
  if (body !== undefined) {
    init.body = typeof body === 'string' ? body : JSON.stringify(body);
    headers['Content-Type'] = 'application/json';
  }
  const res = await fetch(BASE_URL + path, init);
  const text = await res.text();
  let parsed: any = text;
  try {
    parsed = text ? JSON.parse(text) : null;
  } catch {
    /* keep text */
  }
  return { status: res.status, headers: res.headers, body: parsed };
}

export const get = <T = any>(p: string, o?: CallOptions) => call<T>('GET', p, undefined, o);
export const post = <T = any>(p: string, b?: unknown, o?: CallOptions) => call<T>('POST', p, b, o);
export const del = <T = any>(p: string, o?: CallOptions) => call<T>('DELETE', p, undefined, o);

/** Assert an RFC 9457 problem with the given status and SeatLock problem code. */
export function expectProblem(res: Res, status: number, code: string) {
  expect(res.status, JSON.stringify(res.body)).toBe(status);
  expect(res.headers.get('content-type') ?? '').toContain('application/problem+json');
  expect(res.body.type).toBe(`https://seatlock.dev/problems/${code}`);
  expect(res.body.status).toBe(status);
  expect(typeof res.body.title).toBe('string');
}

export interface Seat {
  id: string;
  section: string;
  row: string;
  number: number;
  priceCents: number;
  status: 'available' | 'held' | 'sold';
}

/** Creates a fresh event as olga (box-office) and returns it with its seats. */
export async function createEvent(sections = [{ name: 'Floor', rows: 2, seatsPerRow: 5, priceCents: 2500 }]) {
  const res = await post('/events', { name: `Show ${randomUUID().slice(0, 8)}`, venue: 'Arena', startsAt: '2027-06-01T20:00:00Z', sections }, { as: 'olga' });
  expect(res.status, JSON.stringify(res.body)).toBe(201);
  const seats = await get<{ items: Seat[] }>(`/events/${res.body.id}/seats`);
  return { event: res.body as { id: string; seatCount: number }, seats: seats.body.items };
}

export async function seatStatuses(eventId: string): Promise<Record<string, Seat['status']>> {
  const res = await get<{ items: Seat[] }>(`/events/${eventId}/seats`);
  return Object.fromEntries(res.body.items.map((s) => [s.id, s.status]));
}

export async function hold(as: User, eventId: string, seatIds: string[]) {
  return post('/holds', { eventId, seatIds }, { as });
}

export async function checkout(as: User, holdId: string, key = randomUUID()) {
  return post(`/holds/${holdId}/checkout`, undefined, { as, headers: { 'Idempotency-Key': key } });
}

/** Sends a signed payment webhook, exactly as the provider would. */
export async function webhook(event: unknown, signature?: string) {
  const raw = JSON.stringify(event);
  const sig = signature ?? createHmac('sha256', WEBHOOK_SECRET).update(raw).digest('hex');
  const res = await fetch(BASE_URL + '/webhooks/payments', { method: 'POST', headers: { 'Content-Type': 'application/json', 'X-Signature': sig }, body: raw });
  const text = await res.text();
  return { status: res.status, headers: res.headers, body: text ? JSON.parse(text) : null } as Res;
}

export const paymentEvent = (type: 'payment_intent.succeeded' | 'payment_intent.failed', paymentIntentId: string, id = `evt_${randomUUID()}`) => ({
  id,
  type,
  data: { paymentIntentId },
});

export const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));
