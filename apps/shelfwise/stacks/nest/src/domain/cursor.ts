// Opaque cursors: base64url-encoded JSON. Clients must not rely on the contents (Chapter 12).
import { DomainError } from './errors.js';

export function encodeCursor(value: Record<string, unknown>): string {
  return Buffer.from(JSON.stringify(value)).toString('base64url');
}

export function decodeCursor<T>(raw: string, isValid: (v: unknown) => v is T): T {
  try {
    const value: unknown = JSON.parse(Buffer.from(raw, 'base64url').toString('utf8'));
    if (isValid(value)) return value;
  } catch {
    /* fall through */
  }
  throw new DomainError('bad-cursor', 400, 'Invalid cursor', 'The cursor is malformed or expired; start from the first page.');
}
