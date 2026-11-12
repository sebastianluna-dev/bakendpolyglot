import pg from 'pg';

// Postgres returns bigint (int8) as a string to avoid precision loss; Shelfwise ids fit in a JS number.
pg.types.setTypeParser(20, (v) => Number(v));

export function createPool(connectionString: string) {
  return new pg.Pool({ connectionString, max: 10 });
}

export type Db = pg.Pool;

/** True when err is a Postgres unique violation (23505), optionally on a given constraint. */
export function isUniqueViolation(err: unknown, constraint?: string): boolean {
  const e = err as { code?: string; constraint?: string };
  return e?.code === '23505' && (constraint === undefined || e.constraint === constraint);
}
