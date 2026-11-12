// Tiny HTTP helpers. The suite only speaks HTTP, so it runs against any stack.
import { expect } from 'vitest';

// Not BASE_URL: Vite (and so Vitest) reserves that name and sets it to '/'.
export const BASE_URL = (process.env.SHELFWISE_URL ?? 'http://localhost:3000').replace(/\/$/, '');

export interface Res<T = any> {
  status: number;
  headers: Headers;
  body: T;
}

export async function call<T = any>(method: string, path: string, body?: unknown, headers: Record<string, string> = {}): Promise<Res<T>> {
  const init: RequestInit = { method, headers: { Accept: 'application/json', ...headers } };
  if (body !== undefined) {
    init.body = typeof body === 'string' ? body : JSON.stringify(body);
    (init.headers as Record<string, string>)['Content-Type'] = 'application/json';
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

export const get = <T = any>(p: string, h?: Record<string, string>) => call<T>('GET', p, undefined, h);
export const post = <T = any>(p: string, b?: unknown, h?: Record<string, string>) => call<T>('POST', p, b, h);
export const patch = <T = any>(p: string, b?: unknown, h?: Record<string, string>) => call<T>('PATCH', p, b, h);

/** Assert an RFC 9457 problem with the given status and Shelfwise problem code. */
export function expectProblem(res: Res, status: number, code: string) {
  expect(res.status, JSON.stringify(res.body)).toBe(status);
  expect(res.headers.get('content-type') ?? '').toContain('application/problem+json');
  expect(res.body.type).toBe(`https://shelfwise.dev/problems/${code}`);
  expect(res.body.status).toBe(status);
  expect(typeof res.body.title).toBe('string');
}

const rand = () => Math.random().toString(36).slice(2, 10);
export const uid = rand;

/** A random but valid ISBN-13 shape (the API checks the pattern, not the checksum). */
export const isbn = () => '978' + String(Math.floor(Math.random() * 1e10)).padStart(10, '0');

export async function createBook(overrides: Record<string, unknown> = {}) {
  const res = await post('/books', { title: `Dune ${rand()}`, author: 'Frank Herbert', isbn: isbn(), year: 1965, ...overrides });
  expect(res.status, JSON.stringify(res.body)).toBe(201);
  return res.body as { id: number; isbn: string; title: string; author: string; year: number | null; version: number };
}

export async function createCopy(bookId: number) {
  const res = await post(`/books/${bookId}/copies`, { barcode: `C-${rand()}` });
  expect(res.status, JSON.stringify(res.body)).toBe(201);
  return res.body as { id: number; bookId: number; barcode: string; status: string };
}

export async function createMember() {
  const res = await post('/members', { email: `reader-${rand()}@example.com`, name: 'Ana Reader' });
  expect(res.status, JSON.stringify(res.body)).toBe(201);
  return res.body as { id: number; email: string; name: string };
}
